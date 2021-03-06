/*
 * SonarSource :: Web :: Sonar Plugin
 * Copyright (c) 2010-2016 SonarSource SA and Matthijs Galesloot
 * sonarqube@googlegroups.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sonar.plugins.web.checks.sonar;

import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.plugins.web.checks.AbstractPageCheck;
import org.sonar.plugins.web.checks.RuleTags;
import org.sonar.plugins.web.node.TagNode;
import org.sonar.squidbridge.annotations.NoSqale;

@Rule(
  key = "TableWithoutCaptionCheck",
  name = "\"<table>\" tags should contain a \"<caption>\"",
  priority = Priority.MAJOR,
  tags = {RuleTags.ACCESSIBILITY})
@NoSqale
public class TableWithoutCaptionCheck extends AbstractPageCheck {

  private int tableLine = 0;
  private boolean foundCaption;

  @Override
  public void startElement(TagNode node) {
    if (isTable(node)) {
      foundCaption = false;
      tableLine = node.getStartLinePosition();
    } else if (isCaption(node)) {
      foundCaption = true;
    }
  }

  @Override
  public void endElement(TagNode node) {
    if (isTable(node)) {
      if (!foundCaption && tableLine != 0) {
        createViolation(tableLine, "Add a <caption> tag to this table.");
      }

      foundCaption = false;
      tableLine = 0;
    }
  }

  private static boolean isTable(TagNode node) {
    return "TABLE".equalsIgnoreCase(node.getNodeName());
  }

  private static boolean isCaption(TagNode node) {
    return "CAPTION".equalsIgnoreCase(node.getNodeName());
  }

}
