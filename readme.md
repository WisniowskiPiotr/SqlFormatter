SQLFMT - SQL formatter

Target:
format any sql to a format similar to:
```
CREATE OR REPLACE VIEW {SCHEMA_NAME}.funnel_stages_timeline_with_no_gaps_report AS
    WITH max_lead_stage_number AS (
        SELECT
            lead_id,
            MAX(stage_number) AS max_stage_number
        FROM {SCHEMA_NAME}.funnel_stages_timeline_report
        GROUP BY lead_id
    ), all_stages AS (
        SELECT DISTINCT
            stage,
            stage_number
        FROM {SCHEMA_NAME}.funnel_stages_timeline_report
    )
    SELECT
        funnel_stages_timeline_report.lead_id,
        funnel_stages_timeline_report.contact_id,
        funnel_stages_timeline_report.opportunity_id,
        MAX(CASE
            WHEN funnel_stages_timeline_report.stage = all_stages.stage
                THEN funnel_stages_timeline_report.most_recent_timestamp_est
            ELSE NULL END) AS most_recent_timestamp_est,
        all_stages.stage,
        all_stages.stage_number
    FROM {SCHEMA_NAME}.funnel_stages_timeline_report
    INNER JOIN max_lead_stage_number
        ON max_lead_stage_number.lead_id = funnel_stages_timeline_report.lead_id
    INNER JOIN all_stages
        ON all_stages.stage_number <= max_lead_stage_number.max_stage_number
    GROUP BY
        funnel_stages_timeline_report.lead_id,
        funnel_stages_timeline_report.contact_id,
        funnel_stages_timeline_report.opportunity_id,
        all_stages.stage,
        all_stages.stage_number
WITH NO SCHEMA BINDING
;
```
