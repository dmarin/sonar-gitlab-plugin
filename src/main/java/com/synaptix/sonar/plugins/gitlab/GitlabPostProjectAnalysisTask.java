/*
 * SonarQube :: GitLab Plugin
 * Copyright (C) 2016-2016 Talanlabs
 * gabriel.allaigre@talanlabs.com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package com.synaptix.sonar.plugins.gitlab;

import org.sonar.api.ce.posttask.PostProjectAnalysisTask;
import org.sonar.api.ce.posttask.QualityGate;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

public class GitlabPostProjectAnalysisTask implements PostProjectAnalysisTask {

    private static final Logger LOGGER = Loggers.get(CommitIssuePostJob.class);

    private final GitLabPluginConfiguration gitLabPluginConfiguration;
    private final CommitFacade commitFacade;

    public GitlabPostProjectAnalysisTask(GitLabPluginConfiguration gitLabPluginConfiguration, CommitFacade commitFacade) {
        this.gitLabPluginConfiguration = gitLabPluginConfiguration;
        this.commitFacade = commitFacade;
    }

    @Override
    public void finished(ProjectAnalysis analysis) {
        QualityGate qualityGate = analysis.getQualityGate();
        if (!gitLabPluginConfiguration.isEnabled() || qualityGate == null || qualityGate.getStatus() != QualityGate.Status.ERROR) {
            LOGGER.debug("PostProjectAnalysisTask finished");
            return;
        }

        String qualityGateStatus = "Quality gate failed\n ";
        for (QualityGate.Condition condition : qualityGate.getConditions()) {
            if (condition.getStatus() == QualityGate.EvaluationStatus.ERROR) {
                qualityGateStatus += condition.getMetricKey() + " value of " +condition.getValue() + " being error threshold "+condition.getErrorThreshold() + "\n";
            }
        }
        commitFacade.addGlobalComment(qualityGateStatus);
    }
}
