/**
 * Aptana Studio
 * Copyright (c) 2013 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the GNU Public License (GPL) v3 (with exceptions).
 * Please see the license.html included with this distribution for details.
 * Any modifications to this file must keep this entire header intact.
 */
package com.aptana.git.ui.internal.actions;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.CoreException;

import com.aptana.git.core.github.IGithubPullRequest;
import com.aptana.git.core.github.IGithubRepository;
import com.aptana.git.core.model.GitRepository;
import com.aptana.ui.MenuDialogItem;
import com.aptana.ui.QuickMenuDialog;

/**
 * @author cwilliams
 */
public class ViewPullRequestHandler extends AbstractGithubHandler
{

	@Override
	protected Object doExecute(ExecutionEvent event) throws ExecutionException
	{
		final GitRepository repo = getSelectedRepository();
		if (repo == null)
		{
			throw new ExecutionException(Messages.CreatePullRequestHandler_NoRepoErr);
		}

		IGithubRepository ghRepo = getGithubRepo();
		if (ghRepo == null)
		{
			return null;
		}

		try
		{
			List<IGithubPullRequest> prs = ghRepo.getOpenPullRequests();
			List<MenuDialogItem> listOfMaps = new ArrayList<MenuDialogItem>();
			for (IGithubPullRequest pr : prs)
			{
				listOfMaps.add(new MenuDialogItem(MessageFormat.format("#{0} - {1}", Long.toString(pr.getNumber()), //$NON-NLS-1$
						pr.getTitle())));
			}
			if (!listOfMaps.isEmpty())
			{
				QuickMenuDialog dialog = new QuickMenuDialog(getShell(), Messages.ViewPullRequestHandler_PopupTitle);
				dialog.setInput(listOfMaps);
				if (dialog.open() != -1)
				{
					IGithubPullRequest pr = prs.get(dialog.getReturnCode());
					viewPullRequest(pr);
				}
			}
		}
		catch (CoreException e)
		{
			throw new ExecutionException(e.getMessage(), e);
		}
		return null;
	}
}
