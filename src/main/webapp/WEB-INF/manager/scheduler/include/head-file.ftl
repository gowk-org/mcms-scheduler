
<!--小图标-->
<link rel="stylesheet" href="//at.alicdn.com/t/font_1126286_3v5pfgbsf85.css" />

<script src="${base}/static/plugins/jquery/1.12.4/jquery-1.12.4.min.js"></script>
<!-- 拖拽 -->
<script src="${base}/static/plugins/sortable/1.8.4/Sortable.min.js"></script>
<script src="${base}/static/plugins/VueDraggable/2.20.0/vuedraggable.umd.min.js"></script>

<#--瀑布流-->
<script src="${base}/static/plugins/vue.waterfall2/vue-waterfall2.js"></script>

<!-- vue富文本编辑器 -->
<script src="${base}/static/plugins/vue-quill-editor/quill.min.js"></script>
<script src="${base}/static/plugins/vue-quill-editor/vue-quill-editor.js"></script>
<link href="${base}/static/plugins/quill/1.3.6/quill.snow.css" rel="stylesheet">
<link href="${base}/static/plugins/quill/1.3.6/quill.bubble.css" rel="stylesheet">
<!--百度富文本-->
<script type="text/javascript" charset="utf-8" src="${base}/static/plugins/ueditor/1.4.3.1/ueditor.parse.js"></script>
<script type="text/javascript" charset="utf-8" src="${base}/static/plugins/ueditor/1.4.3.1/ueditor.config.js"></script>
<script type="text/javascript" charset="utf-8" src="${base}/static/plugins/ueditor/1.4.3.1/ueditor.all.js"></script>
<script type="text/javascript" charset="utf-8" src="${base}/static/plugins/ueditor/1.4.3.1/lang/zh-cn/zh-cn.js"></script>
<script src="${base}/static/mweixin/js/common.js"></script>
<#--加载公共样式-->
<link rel="stylesheet" type="text/css" href="${base}/static/mweixin/css/app.css" />
<style>
       [v-cloak] {
            display: none;
        }
        #appForm .iconfont{
        	font-size: 12px;
        	margin-right: 5px;
        }
</style>
<script>
    Vue.use(VueQuillEditor)
    vueWaterfall2.install(Vue);
</script>