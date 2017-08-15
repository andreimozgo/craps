package by.mozgo.craps.command.admin;

import by.mozgo.craps.command.ActionCommand;
import by.mozgo.craps.command.ActionResult;
import by.mozgo.craps.command.ConfigurationManager;
import by.mozgo.craps.services.UserService;
import by.mozgo.craps.services.impl.UserServiceImpl;

import javax.servlet.http.HttpServletRequest;

import static by.mozgo.craps.command.ActionResult.ActionType.FORWARD;

/**
 * Created by Andrei Mozgo. 2017.
 */
public class ChangeRoleCommand implements ActionCommand {
    @Override
    public ActionResult execute(HttpServletRequest request) {
        UserService userService = UserServiceImpl.getInstance();
        String page;
        int id = Integer.parseInt(request.getParameter("user_id"));
        int newRole = Integer.parseInt(request.getParameter("newRole"));
        userService.updateRole(id, newRole);
        page = ConfigurationManager.getProperty("command.adminpage");
        return new ActionResult(FORWARD, page);
    }
}
