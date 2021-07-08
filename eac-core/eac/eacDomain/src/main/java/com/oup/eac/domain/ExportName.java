package com.oup.eac.domain;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class ExportName extends CreatedAudit {

    @Enumerated(EnumType.STRING)
    private ExportType exportType;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false, updatable = false)
    private Question question;
    
    private String name;

    /**
     * @return the exportType
     */
    public ExportType getExportType() {
        return exportType;
    }

    /**
     * @param exportType the exportType to set
     */
    public void setExportType(ExportType exportType) {
        this.exportType = exportType;
    }

    /**
     * @return the question
     */
    public Question getQuestion() {
        return question;
    }

    /**
     * @param question the question to set
     */
    public void setQuestion(Question question) {
        this.question = question;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    
}
