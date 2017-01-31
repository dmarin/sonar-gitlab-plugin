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

import org.sonar.api.Plugin;
import org.sonar.api.PropertyType;
import org.sonar.api.config.PropertyDefinition;
import org.sonar.api.resources.Qualifiers;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import java.util.Arrays;
import java.util.List;

public class GitLabPlugin implements Plugin {

    private static final Logger LOGGER = Loggers.get(GitLabPlugin.class);

    static final String GITLAB_URL = "sonar.gitlab.url";
    static final String GITLAB_MAX_GLOBAL_ISSUES = "sonar.gitlab.max_global_issues";
    static final String GITLAB_USER_TOKEN = "sonar.gitlab.user_token";
    static final String GITLAB_PROJECT_ID = "sonar.gitlab.project_id";
    static final String GITLAB_COMMIT_SHA = "sonar.gitlab.commit_sha";
    static final String GITLAB_REF_NAME = "sonar.gitlab.ref_name";
    static final String GITLAB_IGNORE_FILE = "sonar.gitlab.ignore_file";
    static final String GITLAB_GLOBAL_TEMPLATE = "sonar.gitlab.global_template";
    static final String GITLAB_INLINE_TEMPLATE = "sonar.gitlab.inline_template";
    static final String GITLAB_COMMENT_NO_ISSUE = "sonar.gitlab.comment_no_issue";

    static final String CATEGORY = "gitlab";
    static final String SUBCATEGORY = "reporting";

    private static List<PropertyDefinition> definitions() {
        return Arrays
                .asList(PropertyDefinition
                                .builder(GITLAB_URL)
                                .name("GitLab url")
                                .description("URL to access GitLab.")
                                .category(CATEGORY)
                                .subCategory(SUBCATEGORY)
                                .defaultValue("https://gitlab.com")
                                .index(1)
                                .build(),
                        PropertyDefinition
                                .builder(GITLAB_USER_TOKEN)
                                .name("GitLab User Token")
                                .description("GitLab user token is developer role.")
                                .category(CATEGORY)
                                .subCategory(SUBCATEGORY)
                                .index(2)
                                .build(),
                        PropertyDefinition
                                .builder(GITLAB_MAX_GLOBAL_ISSUES)
                                .name("GitLab Max Global Issues")
                                .description("Max issues to show in global comment.")
                                .category(CATEGORY)
                                .subCategory(SUBCATEGORY)
                                .type(PropertyType.INTEGER)
                                .defaultValue(String.valueOf(10))
                                .index(3)
                                .build(),
                        PropertyDefinition.builder(GITLAB_PROJECT_ID)
                                .name("GitLab Project id")
                                .description("The unique id, path with namespace, name with namespace, web url, ssh url or http url of the current project that GitLab.")
                                .category(CATEGORY)
                                .subCategory(SUBCATEGORY)
                                .index(4)
                                .onlyOnQualifiers(Qualifiers.PROJECT)
                                .build(),
                        PropertyDefinition
                                .builder(GITLAB_COMMIT_SHA)
                                .name("GitLab Commit SHA")
                                .description("The commit revision for which project is built.")
                                .category(CATEGORY)
                                .subCategory(SUBCATEGORY)
                                .index(5)
                                .hidden()
                                .build(),
                        PropertyDefinition
                                .builder(GITLAB_REF_NAME)
                                .name("GitLab Ref Name")
                                .description("The commit revision for which project is built.")
                                .category(CATEGORY).subCategory(SUBCATEGORY)
                                .index(6)
                                .hidden()
                                .build(),
                        PropertyDefinition
                                .builder(GITLAB_IGNORE_FILE)
                                .name("GitLab Ingore file")
                                .description("Ignore issues on files no modified by the commit")
                                .category(CATEGORY)
                                .subCategory(SUBCATEGORY)
                                .type(PropertyType.BOOLEAN)
                                .defaultValue(String.valueOf(false))
                                .index(7)
                                .hidden()
                                .build(),
                        PropertyDefinition
                                .builder(GITLAB_COMMENT_NO_ISSUE)
                                .name("GitLab Comment when no new issue")
                                .description("Add a comment even when there is no new issue.")
                                .category(CATEGORY)
                                .subCategory(SUBCATEGORY)
                                .type(PropertyType.BOOLEAN)
                                .defaultValue(String.valueOf(false))
                                .index(8)
                                .build()/*,
                        PropertyDefinition.builder(GITLAB_GLOBAL_TEMPLATE).name("GitLab Global Template").description("Template for global comment in commit.").category(CATEGORY)
                                .subCategory(SUBCATEGORY).type(PropertyType.TEXT).index(8).build(),
                        PropertyDefinition.builder(GITLAB_INLINE_TEMPLATE).name("GitLab Inline Template").description("Template for inline comment in commit.").category(CATEGORY)
                                .subCategory(SUBCATEGORY).type(PropertyType.TEXT).index(9).build()*/);
    }

    @Override
    public void define(Context context) {
        LOGGER.info("Defining extension");
        context.addExtensions(Arrays.asList(CommitIssuePostJob.class, GitLabPluginConfiguration.class, CommitProjectBuilder.class, CommitFacade.class, InputFileCacheSensor.class, InputFileCache.class,
                MarkDownUtils.class, GitlabPostProjectAnalysisTask.class));
        context.addExtensions(definitions());
    }
}
