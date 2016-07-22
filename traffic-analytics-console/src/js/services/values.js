app.value('columnDefinition', {
    // todo: add namespace for column, like "performance.date"
    // for performance
    keyword: { title: "column.keyword", sortable: false, type: "string", format: "string" },
    adgroup: { title: "column.adgroup", sortable: false, type: "string", format: "string" },
    campaign: { title: "column.campaign", sortable: false, type: "string", format: "string" },
    //date: { title: "column.date", sortable: true, format: ["date","yyyy#MM#dd"] },
    date: { title: "column.date", sortable: true, type: "string", format: "string" },
    clicks: { title: "column.clicks", sortable: true, type: "numeric", format: "number" },
    impressions: { title: "column.impressions", sortable: true, type: "numeric", format: "number" },
    cost: { title: "column.cost", sortable: true, type: "numeric", format: ["number",2] },
    //balance: { title: "column.balance", sortable: false, format: ["currency",'RMB'] },
    revenue: { title: "column.revenue", sortable: true, type: "numeric", format: ["number",2] },
    cpc: { title: "column.cpc", sortable: false, type: "numeric", format: ["number",3] }
});
