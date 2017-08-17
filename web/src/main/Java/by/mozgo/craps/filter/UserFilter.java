package by.mozgo.craps.filter;

import by.mozgo.craps.command.ConfigurationManager;
import by.mozgo.craps.command.CrapsConstant;
import by.mozgo.craps.entity.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by Andrei Mozgo. 2017.
 */
@WebFilter(dispatcherTypes = {DispatcherType.FORWARD, DispatcherType.REQUEST}, urlPatterns = {"/jsp/user/*"})
public class UserFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        User user = (User) httpRequest.getSession().getAttribute(CrapsConstant.USER);
        if (user == null) {
            String page = ConfigurationManager.getProperty("path.page.error.404");
            RequestDispatcher dispatcher = request.getServletContext().getRequestDispatcher(page);
            dispatcher.forward(request, response);
        } else if (user.getUserRole().equals(User.UserRole.BLOCKED)){
            String page = ConfigurationManager.getProperty("path.page.error.blocked");
            RequestDispatcher dispatcher = request.getServletContext().getRequestDispatcher(page);
            dispatcher.forward(request, response);
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
