package com.softline.mbg.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DataCollectionTaskExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public DataCollectionTaskExample() {
        oredCriteria = new ArrayList<>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Long value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Long value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Long value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Long value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Long value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Long value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Long> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Long> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Long value1, Long value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Long value1, Long value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andTaskNameIsNull() {
            addCriterion("task_name is null");
            return (Criteria) this;
        }

        public Criteria andTaskNameIsNotNull() {
            addCriterion("task_name is not null");
            return (Criteria) this;
        }

        public Criteria andTaskNameEqualTo(String value) {
            addCriterion("task_name =", value, "taskName");
            return (Criteria) this;
        }

        public Criteria andTaskNameNotEqualTo(String value) {
            addCriterion("task_name <>", value, "taskName");
            return (Criteria) this;
        }

        public Criteria andTaskNameGreaterThan(String value) {
            addCriterion("task_name >", value, "taskName");
            return (Criteria) this;
        }

        public Criteria andTaskNameGreaterThanOrEqualTo(String value) {
            addCriterion("task_name >=", value, "taskName");
            return (Criteria) this;
        }

        public Criteria andTaskNameLessThan(String value) {
            addCriterion("task_name <", value, "taskName");
            return (Criteria) this;
        }

        public Criteria andTaskNameLessThanOrEqualTo(String value) {
            addCriterion("task_name <=", value, "taskName");
            return (Criteria) this;
        }

        public Criteria andTaskNameLike(String value) {
            addCriterion("task_name like", value, "taskName");
            return (Criteria) this;
        }

        public Criteria andTaskNameNotLike(String value) {
            addCriterion("task_name not like", value, "taskName");
            return (Criteria) this;
        }

        public Criteria andTaskNameIn(List<String> values) {
            addCriterion("task_name in", values, "taskName");
            return (Criteria) this;
        }

        public Criteria andTaskNameNotIn(List<String> values) {
            addCriterion("task_name not in", values, "taskName");
            return (Criteria) this;
        }

        public Criteria andTaskNameBetween(String value1, String value2) {
            addCriterion("task_name between", value1, value2, "taskName");
            return (Criteria) this;
        }

        public Criteria andTaskNameNotBetween(String value1, String value2) {
            addCriterion("task_name not between", value1, value2, "taskName");
            return (Criteria) this;
        }

        public Criteria andTaskDescIsNull() {
            addCriterion("task_desc is null");
            return (Criteria) this;
        }

        public Criteria andTaskDescIsNotNull() {
            addCriterion("task_desc is not null");
            return (Criteria) this;
        }

        public Criteria andTaskDescEqualTo(String value) {
            addCriterion("task_desc =", value, "taskDesc");
            return (Criteria) this;
        }

        public Criteria andTaskDescNotEqualTo(String value) {
            addCriterion("task_desc <>", value, "taskDesc");
            return (Criteria) this;
        }

        public Criteria andTaskDescGreaterThan(String value) {
            addCriterion("task_desc >", value, "taskDesc");
            return (Criteria) this;
        }

        public Criteria andTaskDescGreaterThanOrEqualTo(String value) {
            addCriterion("task_desc >=", value, "taskDesc");
            return (Criteria) this;
        }

        public Criteria andTaskDescLessThan(String value) {
            addCriterion("task_desc <", value, "taskDesc");
            return (Criteria) this;
        }

        public Criteria andTaskDescLessThanOrEqualTo(String value) {
            addCriterion("task_desc <=", value, "taskDesc");
            return (Criteria) this;
        }

        public Criteria andTaskDescLike(String value) {
            addCriterion("task_desc like", value, "taskDesc");
            return (Criteria) this;
        }

        public Criteria andTaskDescNotLike(String value) {
            addCriterion("task_desc not like", value, "taskDesc");
            return (Criteria) this;
        }

        public Criteria andTaskDescIn(List<String> values) {
            addCriterion("task_desc in", values, "taskDesc");
            return (Criteria) this;
        }

        public Criteria andTaskDescNotIn(List<String> values) {
            addCriterion("task_desc not in", values, "taskDesc");
            return (Criteria) this;
        }

        public Criteria andTaskDescBetween(String value1, String value2) {
            addCriterion("task_desc between", value1, value2, "taskDesc");
            return (Criteria) this;
        }

        public Criteria andTaskDescNotBetween(String value1, String value2) {
            addCriterion("task_desc not between", value1, value2, "taskDesc");
            return (Criteria) this;
        }

        public Criteria andImportDataSourceIdIsNull() {
            addCriterion("import_data_source_id is null");
            return (Criteria) this;
        }

        public Criteria andImportDataSourceIdIsNotNull() {
            addCriterion("import_data_source_id is not null");
            return (Criteria) this;
        }

        public Criteria andImportDataSourceIdEqualTo(Long value) {
            addCriterion("import_data_source_id =", value, "importDataSourceId");
            return (Criteria) this;
        }

        public Criteria andImportDataSourceIdNotEqualTo(Long value) {
            addCriterion("import_data_source_id <>", value, "importDataSourceId");
            return (Criteria) this;
        }

        public Criteria andImportDataSourceIdGreaterThan(Long value) {
            addCriterion("import_data_source_id >", value, "importDataSourceId");
            return (Criteria) this;
        }

        public Criteria andImportDataSourceIdGreaterThanOrEqualTo(Long value) {
            addCriterion("import_data_source_id >=", value, "importDataSourceId");
            return (Criteria) this;
        }

        public Criteria andImportDataSourceIdLessThan(Long value) {
            addCriterion("import_data_source_id <", value, "importDataSourceId");
            return (Criteria) this;
        }

        public Criteria andImportDataSourceIdLessThanOrEqualTo(Long value) {
            addCriterion("import_data_source_id <=", value, "importDataSourceId");
            return (Criteria) this;
        }

        public Criteria andImportDataSourceIdIn(List<Long> values) {
            addCriterion("import_data_source_id in", values, "importDataSourceId");
            return (Criteria) this;
        }

        public Criteria andImportDataSourceIdNotIn(List<Long> values) {
            addCriterion("import_data_source_id not in", values, "importDataSourceId");
            return (Criteria) this;
        }

        public Criteria andImportDataSourceIdBetween(Long value1, Long value2) {
            addCriterion("import_data_source_id between", value1, value2, "importDataSourceId");
            return (Criteria) this;
        }

        public Criteria andImportDataSourceIdNotBetween(Long value1, Long value2) {
            addCriterion("import_data_source_id not between", value1, value2, "importDataSourceId");
            return (Criteria) this;
        }

        public Criteria andExportDataSourceIdIsNull() {
            addCriterion("export_data_source_id is null");
            return (Criteria) this;
        }

        public Criteria andExportDataSourceIdIsNotNull() {
            addCriterion("export_data_source_id is not null");
            return (Criteria) this;
        }

        public Criteria andExportDataSourceIdEqualTo(Long value) {
            addCriterion("export_data_source_id =", value, "exportDataSourceId");
            return (Criteria) this;
        }

        public Criteria andExportDataSourceIdNotEqualTo(Long value) {
            addCriterion("export_data_source_id <>", value, "exportDataSourceId");
            return (Criteria) this;
        }

        public Criteria andExportDataSourceIdGreaterThan(Long value) {
            addCriterion("export_data_source_id >", value, "exportDataSourceId");
            return (Criteria) this;
        }

        public Criteria andExportDataSourceIdGreaterThanOrEqualTo(Long value) {
            addCriterion("export_data_source_id >=", value, "exportDataSourceId");
            return (Criteria) this;
        }

        public Criteria andExportDataSourceIdLessThan(Long value) {
            addCriterion("export_data_source_id <", value, "exportDataSourceId");
            return (Criteria) this;
        }

        public Criteria andExportDataSourceIdLessThanOrEqualTo(Long value) {
            addCriterion("export_data_source_id <=", value, "exportDataSourceId");
            return (Criteria) this;
        }

        public Criteria andExportDataSourceIdIn(List<Long> values) {
            addCriterion("export_data_source_id in", values, "exportDataSourceId");
            return (Criteria) this;
        }

        public Criteria andExportDataSourceIdNotIn(List<Long> values) {
            addCriterion("export_data_source_id not in", values, "exportDataSourceId");
            return (Criteria) this;
        }

        public Criteria andExportDataSourceIdBetween(Long value1, Long value2) {
            addCriterion("export_data_source_id between", value1, value2, "exportDataSourceId");
            return (Criteria) this;
        }

        public Criteria andExportDataSourceIdNotBetween(Long value1, Long value2) {
            addCriterion("export_data_source_id not between", value1, value2, "exportDataSourceId");
            return (Criteria) this;
        }

        public Criteria andIsNeedMapperIsNull() {
            addCriterion("is_need_mapper is null");
            return (Criteria) this;
        }

        public Criteria andIsNeedMapperIsNotNull() {
            addCriterion("is_need_mapper is not null");
            return (Criteria) this;
        }

        public Criteria andIsNeedMapperEqualTo(Integer value) {
            addCriterion("is_need_mapper =", value, "isNeedMapper");
            return (Criteria) this;
        }

        public Criteria andIsNeedMapperNotEqualTo(Integer value) {
            addCriterion("is_need_mapper <>", value, "isNeedMapper");
            return (Criteria) this;
        }

        public Criteria andIsNeedMapperGreaterThan(Integer value) {
            addCriterion("is_need_mapper >", value, "isNeedMapper");
            return (Criteria) this;
        }

        public Criteria andIsNeedMapperGreaterThanOrEqualTo(Integer value) {
            addCriterion("is_need_mapper >=", value, "isNeedMapper");
            return (Criteria) this;
        }

        public Criteria andIsNeedMapperLessThan(Integer value) {
            addCriterion("is_need_mapper <", value, "isNeedMapper");
            return (Criteria) this;
        }

        public Criteria andIsNeedMapperLessThanOrEqualTo(Integer value) {
            addCriterion("is_need_mapper <=", value, "isNeedMapper");
            return (Criteria) this;
        }

        public Criteria andIsNeedMapperIn(List<Integer> values) {
            addCriterion("is_need_mapper in", values, "isNeedMapper");
            return (Criteria) this;
        }

        public Criteria andIsNeedMapperNotIn(List<Integer> values) {
            addCriterion("is_need_mapper not in", values, "isNeedMapper");
            return (Criteria) this;
        }

        public Criteria andIsNeedMapperBetween(Integer value1, Integer value2) {
            addCriterion("is_need_mapper between", value1, value2, "isNeedMapper");
            return (Criteria) this;
        }

        public Criteria andIsNeedMapperNotBetween(Integer value1, Integer value2) {
            addCriterion("is_need_mapper not between", value1, value2, "isNeedMapper");
            return (Criteria) this;
        }

        public Criteria andStatusIsNull() {
            addCriterion("status is null");
            return (Criteria) this;
        }

        public Criteria andStatusIsNotNull() {
            addCriterion("status is not null");
            return (Criteria) this;
        }

        public Criteria andStatusEqualTo(String value) {
            addCriterion("status =", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotEqualTo(String value) {
            addCriterion("status <>", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThan(String value) {
            addCriterion("status >", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThanOrEqualTo(String value) {
            addCriterion("status >=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThan(String value) {
            addCriterion("status <", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThanOrEqualTo(String value) {
            addCriterion("status <=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLike(String value) {
            addCriterion("status like", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotLike(String value) {
            addCriterion("status not like", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusIn(List<String> values) {
            addCriterion("status in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotIn(List<String> values) {
            addCriterion("status not in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusBetween(String value1, String value2) {
            addCriterion("status between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotBetween(String value1, String value2) {
            addCriterion("status not between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNull() {
            addCriterion("create_time is null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNotNull() {
            addCriterion("create_time is not null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeEqualTo(Date value) {
            addCriterion("create_time =", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotEqualTo(Date value) {
            addCriterion("create_time <>", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThan(Date value) {
            addCriterion("create_time >", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("create_time >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThan(Date value) {
            addCriterion("create_time <", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(Date value) {
            addCriterion("create_time <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIn(List<Date> values) {
            addCriterion("create_time in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotIn(List<Date> values) {
            addCriterion("create_time not in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeBetween(Date value1, Date value2) {
            addCriterion("create_time between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotBetween(Date value1, Date value2) {
            addCriterion("create_time not between", value1, value2, "createTime");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {
        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}