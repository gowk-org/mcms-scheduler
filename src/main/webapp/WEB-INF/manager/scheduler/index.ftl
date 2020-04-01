<!DOCTYPE html>
<html>

<head>
    <title>公众号列表</title>
    <#include "/include/head-file.ftl" />
	<style>
		[v-cloak] {
			display: none;
		}
		.ms-container {
			height: calc(100% - 62px);
		}
		.ms-table-pagination {
		   height: calc(100vh - 156px);
	   }
	</style>
</head>
<body>
	<div id="index"   v-cloak>
		<el-header class="ms-header" height="50px">
			<el-col :span="12">
				<@shiro.hasPermission name="weixin:save"><el-button type="primary" icon="el-icon-plus" size="mini" @click="openForm()">新增</el-button></@shiro.hasPermission>
				<@shiro.hasPermission name="weixin:del"><el-button type="danger" icon="el-icon-delete" size="mini" @click="del()" >删除</el-button></@shiro.hasPermission>
			</el-col>
		</el-header>
		<el-main class="ms-container">
			<el-table v-loading="loading" class="ms-table-pagination" ref="multipleTable" border="true"  :data="dataList" tooltip-effect="dark" @selection-change="handleSelectionChange" :max-height="tableHeight" >
				<template slot="empty">
					{{emptyText}}
				</template>
                <el-table-column type="selection" width="50"></el-table-column>
                <el-table-column label="编号" width="100" align="center"><template slot-scope="scope">{{ scope.row.weixinId }}</template></el-table-column>
                <el-table-column label="公众号名称" width="220">
	                <template slot-scope="scope">
						<@shiro.hasPermission name="weixin:edit">
							{{ scope.row.weixinName }}
						</@shiro.hasPermission>
						<@shiro.lacksPermission name="weixin:edit">
							{{ scope.row.weixinName }}
						</@shiro.lacksPermission>
					</template>
                </el-table-column>
                <el-table-column label="微信号" width="220">
                	<template slot-scope="scope">
						<@shiro.hasPermission name="weixin:update">
                			{{ scope.row.weixinNo }}
						</@shiro.hasPermission>
						<@shiro.lacksPermission name="weixin:update">
							{{ scope.row.weixinNo }}
						</@shiro.lacksPermission>
					</template>
                </el-table-column>
                <el-table-column label="公众号类型" width="120" align="center">
                	<template slot-scope="scope">
                		<template v-if="scope.row.weixinType==0">
                			服务号 
                		</template>
                		<template v-if="scope.row.weixinType==1">
                			订阅号
                		</template>
                	</template>
                </el-table-column>
                <el-table-column label="微信token"><template slot-scope="scope">{{ scope.row.weixinToken }}</template></el-table-column>
            	<el-table-column label="操作" align="center" width="180">
            		<template slot-scope="scope">
	            		<a :href="ms.manager + '/mweixin/'+scope.row.weixinId+'/function.do'">进入</a>
	            		<a :href="ms.manager + '/mweixin/form.do?weixinId='+scope.row.weixinId">编辑</a>
            		</template>
            	</el-table-column>
            </el-table>
            <el-pagination background :page-sizes="[5, 10, 20]" layout="total, sizes, prev, pager, next, jumper" :current-page="currentPage"  :page-size="pagesize"  :total="total" class="ms-pagination" @current-change='currentChange' @size-change="sizeChange">
            </el-pagination>
		</el-main> 
	</div>
</body>

</html>
<script>
var indexVue = new Vue({
	el: '#index',
	data:{
		dataList: [], //微信列表
		selData: [], //选中列表
		total: 0, //总记录数量
        pagesize: 10, //页面数量
        currentPage:1, //初始页
        mananger: ms.manager,
		loading:true,
		emptyText:'',
	},
	methods: {
		//查询列表
		list: function() {
			var that = this;
			setTimeout(()=>{
				ms.http.get(ms.manager + "/mweixin/list.do").then(function(data){
					if(data.data.total <=0){
						that.loading = false;
						that.emptyText='暂无数据'
						that.dataList =[];
					}else{
						that.emptyText='';
						that.loading = false;
						that.total = data.data.total;
						that.dataList = data.data.rows.reverse();
					}
				}).catch(function(err){
					console.log(err);
				});
			},500);
		},
	    //选中行，selection返回每一行选中行的对象
        handleSelectionChange: function(selection) {
            this.selData = selection;
        },
        //删除
        del: function(){
        	var that = this;
        	that.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
					    	confirmButtonText: '确定',
					    	cancelButtonText: '取消',
					    	type: 'warning'
					    }).then(() => {
					    	ms.http.post(ms.manager + "/mweixin/delete.do", this.selData,{
            					headers: {
                					'Content-Type': 'application/json'
                				}
            				}).then(
	            				function(data){
		            				if (data.result) { 	
										that.$notify({
						     				type: 'success',
						        			message: '删除成功!'
						    			});
					    				//删除成功，刷新列表
					      				that.list();
					      			}else {
										that.$notify({
											title: '失败',
											message: data.msg,
											type: 'warning'
										});
									}
	            				});
					    }).catch(() => {
					    	that.$notify({
					        	type: 'info',
					        	message: '已取消删除'
					    	});          
				    	});	            	
        },
        //新增
        openForm:function(){
        	location.href = ms.manager + "/mweixin/form.do";
        },
        //pageSize改变时会触发
        sizeChange:function(pagesize) {
			this.loading=true;
            this.pagesize = pagesize;
            this.list();
        },
        //currentPage改变时会触发
        currentChange:function(currentPage) {
			this.loading=true;
            this.currentPage = currentPage;
            this.list();
        },
	},	
	mounted(){
		this.list();
	},
	computed:{
		//表格最大高度 用来自适应
		tableHeight:function () {
			return document.documentElement.clientHeight - 100;
		}
	},
})
</script>