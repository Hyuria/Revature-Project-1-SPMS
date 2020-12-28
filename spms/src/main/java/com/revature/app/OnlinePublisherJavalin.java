package com.revature.app;

import com.revature.beans.Author;
import com.revature.beans.Editor;
import com.revature.controller.AuthorController;
import com.revature.controller.EditorController;
import com.revature.controller.UserController;
import io.javalin.Javalin;
import org.eclipse.jetty.server.session.SessionHandler;

import java.util.function.Supplier;

import static io.javalin.apibuilder.ApiBuilder.*;

public class OnlinePublisherJavalin {

    public static void main(String[] args) {
        Javalin app = Javalin.create((config) -> {
           config.addStaticFiles("/static");
           config.enableCorsForAllOrigins();
        });

        app.start(8080);

        app.routes(() -> {

            path("users", () -> {
                get(UserController::checkLogin);
                put(UserController::login);
                post(UserController::registerUser);
                delete(UserController::logout);
                path(":id", () -> {
                    get(UserController::getAuthorEditor);
                });
            });
            path("author",() ->{
                put(AuthorController::updatePitch);
                post(AuthorController::submitPitch);

                path("stories", () -> {
                   get(AuthorController::getStories);
                });
                path("messages", () -> {
                    get(AuthorController::getMessages);
                });
            });
            path("editor",() ->{

                path("approvals", () -> {
                    get(EditorController::getApprovals);
                    path(":id", () -> {
                        put(EditorController::approvalStory);
                    });
                });

                path("requests", () -> {
                    get(EditorController::getRequests);
                    post(EditorController::addRequests);
                });

            });
            path("committee",() ->{
                get(EditorController::getCommittee);
            });

        });

    }

    private static Supplier<SessionHandler> customSessionHandlerSupplier() {
        final SessionHandler sessionHandler = new SessionHandler();
        // [..] add persistence DB etc. management here [..]
        sessionHandler.getSessionCookieConfig().setHttpOnly(false);
        sessionHandler.getSessionCookieConfig().setSecure(false);
        sessionHandler.getSessionCookieConfig().setComment("__SAME_SITE_NONE__");
        return () -> sessionHandler;
    }

}
