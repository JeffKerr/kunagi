/*
 * Copyright 2011 Witoslaw Koczewsi <wi@koczewski.de>, Artjom Kochtchi
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero
 * General Public License as published by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public
 * License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package scrum.server.common;

import ilarkesto.base.Str;
import ilarkesto.core.logging.Log;
import ilarkesto.io.IO;
import ilarkesto.ui.web.HtmlBuilder;
import ilarkesto.webapp.RequestWrapper;

import java.io.IOException;

import javax.servlet.ServletConfig;

import scrum.server.ScrumWebApplication;
import scrum.server.WebSession;

public class StartServlet extends AKunagiServlet {

	private static Log log = Log.get(StartServlet.class);

	private static boolean first = true;

	@Override
	protected void onRequest(RequestWrapper<WebSession> req) throws IOException {

		if (first) {
			first = false;
			String requestUrl = req.getUri();
			requestUrl = Str.removeSuffix(requestUrl, "index.html");
			if (!systemConfig.isUrlSet()) systemConfig.setUrl(requestUrl);
		}

		if (req.getSession().getUser() == null) {
			redirectToLogin(req);
			return;
		}

		String charset = IO.UTF_8;
		req.setContentTypeHtml();

		req.write(new AKunagiHtmlPageBuilder() {

			@Override
			protected void headerContent(HtmlBuilder html) {
				super.headerContent(html);
				html.SCRIPTjavascript("codemirror/js/codemirror.js", null);
			}

			@Override
			protected void workspaceContainer(HtmlBuilder html) {}

			@Override
			protected String getGwtName() {
				return "scrum.ScrumGwtApplication";
			}

		}.toString());

		HtmlBuilder html = new HtmlBuilder(req.getWriter(), charset);
		html.startHTMLstandard();

		String title = "Kunagi";
		if (config.isShowRelease()) title += " " + applicationInfo.getRelease();
		if (systemConfig.isInstanceNameSet()) title += " @ " + systemConfig.getInstanceName();
		html.startHEAD(title, "EN");
		html.META("X-UA-Compatible", "IE=edge");
		html.LINKfavicon();
		html.startSTYLEcss();
		html.html(getCss());
		html.endSTYLE();
		html.SCRIPTjavascript("/scrum.ScrumGwtApplication.nocache.js", null);
		html.SCRIPTjavascript("codemirror/js/codemirror.js", null);
		html.endHEAD();

		html.startBODY().setId("kunagi");
		html.comment(applicationInfo.toString());
		String analyticsId = systemConfig.getGoogleAnalyticsId();
		if (analyticsId != null) html.googleAnalytics(analyticsId);
		html.endBODY();

		html.endHTML();
		html.flush();
	}

	@Override
	protected void onPreInit(ServletConfig servletConfig) {
		super.onPreInit(servletConfig);
		ScrumWebApplication.get(servletConfig);
	}

}
