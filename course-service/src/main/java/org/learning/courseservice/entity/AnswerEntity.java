/**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-04-06 11:49:32
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-04-06 11:57:26
 * @FilePath: course-service/src/main/java/org/learning/courseservice/entity/AnswerEntity.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package org.learning.courseservice.entity;

import java.util.List;

public class AnswerEntity {
    private List<Radio> radio;
    private List<Judge> judge;
    private List<Choice> choice;

    public List<Radio> getRadio() {
        return radio;
    }

    public void setRadio(List<Radio> radio) {
        this.radio = radio;
    }

    public List<Judge> getJudge() {
        return judge;
    }

    public void setJudge(List<Judge> judge) {
        this.judge = judge;
    }

    public List<Choice> getChoice() {
        return choice;
    }

    public void setChoice(List<Choice> choice) {
        this.choice = choice;
    }
}

