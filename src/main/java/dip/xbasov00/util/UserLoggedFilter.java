/* 
 * Project       : Master Thesis - Portlets based time management tools
 * Document      : UserLoggedFilter.java
 * Author        : Bc. Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

package dip.xbasov00.util;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.filter.FilterChain;
import javax.portlet.filter.FilterConfig;

/**
 * Class UserLoggedFilter ensures check if user is logged.
 * If not it throws UnloggedUserException.
 *
 * @author Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 *
 */
public class UserLoggedFilter implements javax.portlet.filter.RenderFilter {

    @Override
    public void init(FilterConfig filterConfig) throws PortletException { }

    @Override
    public void destroy() { }

    @Override
    public void doFilter(RenderRequest request, RenderResponse response,
            FilterChain chain) throws IOException, PortletException {

        if (request.getRemoteUser() == null) {
            PrintWriter out = response.getWriter();
            out.println(
                "<br /><center><strong style=\"color: red; \">" +
                    "Before start using this portlet please login!" +
                "</strong></center><br />"
            );
        }
        else {
            chain.doFilter(request, response);
        }

    }
}
