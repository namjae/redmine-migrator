package net.kaleidos.taiga.mapper

import net.kaleidos.domain.Issue
import net.kaleidos.taiga.common.DateConversions

class IssueMapper implements Mapper<Issue>, DateConversions {

    @Override
    Map map(Issue issue) {
        [
            type        : issue.type,
            status      : issue.status,
            priority    : issue.priority,
            severity    : issue.severity,
            subject     : issue.subject,
            description : issue.description,
            project     : issue.project.id,
            ref         : issue.ref,
            owner       : issue.owner,
            created_date: format(issue.createdDate),
            attachments : issue.attachments.collect { Mappers.map(it) },
        ]
    }
}