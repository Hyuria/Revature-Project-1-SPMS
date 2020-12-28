package com.revature.controller;

import com.revature.beans.*;
import com.revature.data.*;
import io.javalin.http.Context;

import javax.persistence.criteria.CriteriaBuilder;
import java.net.CookieHandler;
import java.util.*;


public class EditorController {
    private static UserHibernate userHibernate = new UserHibernate();
    private static AuthorHibernate authorHibernate = new AuthorHibernate();
    private static EditorHibernate editorHibernate = new EditorHibernate();
    private static ApprovalHibernate approvalHibernate = new ApprovalHibernate();
    private static CommitteeHibernate committeeHibernate = new CommitteeHibernate();
    private static StoryHibernate storyHibernate = new StoryHibernate();
    private static RequestHibernate requestHibernate = new RequestHibernate();
    private static StatusHibernate statusHibernate = new StatusHibernate();

    public static void getCommittee(Context ctx) {
        Editor editor = ctx.sessionAttribute("editor");
        System.out.println("Current Editor in Session: " + editor);
        Set<Committee> retCommittee = new HashSet<>();
        Set<Committee> committeeSet = committeeHibernate.getAll();
        for (Committee c : committeeSet){
            if (c.getEditorSet().contains(editor)){
                retCommittee.add(c);
            }
        }
        if (retCommittee != null){
            System.out.println("Editor found in committees: " +retCommittee);
            ctx.status(200);
            ctx.json(retCommittee);
            ctx.sessionAttribute("committee", retCommittee);
        }else{
            ctx.status(400);
        }
    }


    public static void getApprovals(Context ctx){
        System.out.println("In getApprovals()");
        Set<Story> storySet = storyHibernate.getAll();
        Set<Approval> approvalSet = approvalHibernate.getAll();
        Set<Story> filteredSet = new HashSet<>();
        Set<Committee> committeeSet = ctx.sessionAttribute("committee");
        for (Committee c : committeeSet){
            System.out.println("Committee of genre:" + c.getGenre().getGenre());
            for (Approval a : approvalSet){
                if (a.getStory().getCommittee().getId() == c.getId()){
                    filteredSet.add(a.getStory());
                }
            }
            for (Story s : storySet){
                if (s.getCommittee().getId() == c.getId()){
                    filteredSet.add(s);
                }
            }
        }
        System.out.println("Fetched Committee Approvals \n" + filteredSet);
        ctx.status(200);
        ctx.json(filteredSet);
    }


    public static void getRequests(Context ctx) {
        System.out.println("In getRequests()");
        Set<Request> requestSet = requestHibernate.getAll();
        Set<Request> filteredSet = new HashSet<>();
        Editor editor = ctx.sessionAttribute("editor");
        if (editor == null){
            System.out.println("Author object is null.");
            ctx.status(400);
        }else{
            for (Request r : requestSet){
                if (r.getSender().getId() == editor.getUser().getId()){
                    filteredSet.add(r);
                }
                if (r.getReceiver().getId() == editor.getUser().getId()){
                    filteredSet.add(r);
                }
            }
            ctx.status(200);
            ctx.json(filteredSet);
        }
    }

    public static void addRequests(Context ctx) {
        System.out.println("Submit a request.");
        Request request = ctx.bodyAsClass(Request.class);
        System.out.println(request);
        Request retRequest = requestHibernate.add(request);
        if (retRequest == null){
            System.out.println("Error with submitted request.");
            ctx.status(400);
        }else{
            System.out.println("Successfully submitted request.");
            ctx.status(200);
        }

    }

    public static void approvalStory(Context ctx) {
        System.out.println("Approval a Story.");
        Integer id = Integer.valueOf(ctx.pathParam("id"));
        System.out.println("Moving Story #" + id + " To next step of the approval process.");
        Story story = storyHibernate.getById(id);
        Editor editor = ctx.sessionAttribute("editor");
        Set<Committee> committeeSet = committeeHibernate.getAll();
        if (story == null){
            System.out.println("Error, invalid story id of " + id);
            ctx.status(400);
        }else{
            Integer approvalStage = story.getStatus().getId();
            Status nextStage = statusHibernate.getById(approvalStage + 1);

            if (nextStage.getId() == 2){

                List<Committee> tempCommitteeList = new ArrayList<>(committeeSet);
                Collections.shuffle(tempCommitteeList);
                for (Committee c : tempCommitteeList){
                    if (c.getGenre().getId() != story.getGenre().getId()){
                        story.setCommittee(c);
                        break;
                    }
                }
                story.setStatus(nextStage);
            }else if (nextStage.getId() == 3){
                for (Committee c : committeeSet){
                    if (c.getGenre().getId() == story.getGenre().getId()){
                        story.setCommittee(c);
                        break;
                    }
                }
                story.setStatus(nextStage);
            }else if (nextStage.getId() == 4){
                System.out.println("Adding entries in approval table for final draft.");
                switch (story.getType().getId()){
                    case 1: // Novel: need a majority of editors in committee;
                    case 2: // Novella: need a majority of editors in committee
                            finalApprovalNovel(story.getId());
                            break;
                    case 3: // Short Story: Senior editor and one editor
                            finalApprovalShortStory(story.getId());
                            break;
                    case 4: // Article: Only senior editor
                            finalApprovalArticle(story.getId());
                            break;
                    default:
                            break;
                }
                story.setStatus(nextStage);
            }else if (nextStage.getId() == 5){
                System.out.println("Checking if other approvals for final draft exist.");
                Boolean existOtherApprovals = false;
                for (Approval a : approvalHibernate.getAll()){
                    if (a.getStory().getId() == story.getId()){
                        if (a.getEditor().getId() != editor.getId()){
                            System.out.println("Another approval for this final draft is required.");
                            existOtherApprovals = true;
                        }else {
                            approvalHibernate.delete(a);
                        }
                    }
                }
                if (!existOtherApprovals){
                    story.setStatus(nextStage);
                }
            }
            storyHibernate.update(story);
            System.out.println("Moved Story to next stage of approval.");
            ctx.status(200);
        }
    }

    public static void finalApprovalNovel(Integer story_id){
        System.out.println("In Final Approval: Novel.");
        // Article: Majority of editors
        Story story = storyHibernate.getById(story_id);
        Set<Editor> editorSet = committeeHibernate.getById(story.getCommittee().getId()).getEditorSet();
        Integer numberOfEditors = editorSet.size();
        List<Editor> editorList = new ArrayList<>(editorSet);
        Collections.shuffle(editorList);
        for (int i = 0; i<numberOfEditors/2+1; i++){
            Approval approval = new Approval();
            approval.setApproval(0);
            approval.setEditor(editorList.get(i));
            approval.setStory(story);
            approvalHibernate.add(approval);
        }
    }

    public static void finalApprovalShortStory(Integer story_id){
        System.out.println("In Final Approval: Short Story.");
        // Article: Only senior editor and one random editor
        Story story = storyHibernate.getById(story_id);
        Set<Editor> editorSet = committeeHibernate.getById(story.getCommittee().getId()).getEditorSet();
        List<Editor> editorList = new ArrayList<>(editorSet);
        Collections.shuffle(editorList);
        for (Editor e : editorList){
            if(e.getRole().getId() == 3){
                Approval approval = new Approval();
                approval.setStory(story);
                approval.setEditor(e);
                approval.setApproval(0);
                approvalHibernate.add(approval);
                break;
            }
        }
        for (Editor e : editorList){
            if(e.getRole().getId() != 3){
                Approval approval = new Approval();
                approval.setStory(story);
                approval.setEditor(e);
                approval.setApproval(0);
                approvalHibernate.add(approval);
                break;
            }
        }

    }

    public static void finalApprovalArticle(Integer story_id){
        System.out.println("In Final Approval: Article.");
        // Article: Only senior editor
        Story story = storyHibernate.getById(story_id);
        Editor editor = new Editor();
        Set<Editor> editorSet = committeeHibernate.getById(story.getCommittee().getId()).getEditorSet();
        for (Editor e : editorSet){
            if(e.getRole().getId() == 3){
                editor = e;
            }
        }
        Approval approval = new Approval();
        approval.setStory(story);
        approval.setEditor(editor);
        approval.setApproval(0);
        approvalHibernate.add(approval);
    }
}
