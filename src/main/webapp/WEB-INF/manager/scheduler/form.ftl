<!DOCTYPE html>
<html>
<head>
	 <title>任务</title>
        <#include "../../include/head-file.ftl">
</head>
<body>
	<div id="form" v-loading="loading" v-cloak>
		<el-header class="ms-header ms-tr" height="50px">
			<el-button type="primary" icon="iconfont icon-baocun" size="mini" @click="save()" :loading="saveDisabled">保存</el-button>
			<el-button size="mini" icon="iconfont icon-fanhui" plain onclick="javascript:history.go(-1)">返回</el-button>
		</el-header>
		<el-main class="ms-container">
            <el-form ref="form" :model="form" :rules="rules" label-width="120px" label-position="right" size="small">
            <el-form-item  label="任务名称" prop="jobName">
                <el-input
                        v-model="form.jobName"
                        :disabled="false"
                          :readonly="false"
                          :style="{width:  '100%'}"
                          :clearable="true"
                          placeholder="请输入任务名称">
                </el-input>
            </el-form-item>
            <el-form-item  label="组名称" prop="jobGroup">
                <el-input
                        v-model="form.jobGroup"
                        :disabled="false"
                          :readonly="false"
                          :style="{width:  '100%'}"
                          :clearable="true"
                          placeholder="请输入组名称">
                </el-input>
            </el-form-item>
            <el-form-item  label="CRON表达式" prop="jobCron">
                <el-input
                        v-model="form.jobCron"
                        :disabled="false"
                          :readonly="false"
                          :style="{width:  '100%'}"
                          :clearable="true"
                          placeholder="请输入CRON表达式">
                </el-input>
            </el-form-item>
            <el-form-item  label="是否启用" prop="jobState">
                    <el-select  v-model="form.jobState"
                               :style="{width: '100%'}"
                               :filterable="false"
                               :disabled="false"
                               :multiple="false" :clearable="true"
                               placeholder="请选择是否启用">
                        <el-option v-for='item in jobStateOptions' :key="item.value" :value="item.value"
                                   :label="item.value"></el-option>
                    </el-select>
            </el-form-item>
            </el-form>

		</el-main>
	</div>
	</body>
	</html>
<script>
        var form = new Vue({
        el: '#form',
        data:function() {
            return {
                loading:false,
                saveDisabled: false,
                //表单数据
                form: {
                    // 任务名称
                    jobName:'',
                    // 组名称
                    jobGroup:'',
                    // CRON表达式
                    jobCron:'',
                    // 是否启用
                    jobState:'启用',
                },
                jobStateOptions:[{"value":"启用"},{"value":"停用"}],
                rules:{
                },

            }
        },
        watch:{
        },
        computed:{   
        },
        methods: {
            save:function() {
                var that = this;
                var url = ms.manager + "/scheduler/task/save.do"
                if (that.form.id > 0) {
                    url = ms.manager + "/scheduler/task/update.do";
                }
                this.$refs.form.validate(function(valid) {
                    if (valid) {
                        that.saveDisabled = true;
                        var data = JSON.parse(JSON.stringify(that.form));
                        ms.http.post(url, data).then(function (data) {
                            if (data.result) {
                                that.$notify({
                                    title: '成功',
                                    message: '保存成功',
                                    type: 'success'
                                });
                                location.href = ms.manager + "/scheduler/task/index.do";
                            } else {
                                that.$notify({
                                    title: '失败',
                                    message: data.msg,
                                    type: 'warning'
                                });
                            }
                            that.saveDisabled = false;
                        });
                    } else {
                        return false;
                    }
                })
            },

            //获取当前任务
            get:function(id) {
                var that = this;
                this.loading = true
                ms.http.get(ms.manager + "/scheduler/task/get.do", {"id":id}).then(function (res) {
                    that.loading = false
                    if(res.result&&res.data){
                        that.form = res.data;
                    }
                }).catch(function (err) {
                    console.log(err);
                });
            },
        },
        created:function() {
            var that = this;
            this.form.id = ms.util.getParameter("id");
            if (this.form.id) {
                this.get(this.form.id);
            }
        }
    });
</script>
