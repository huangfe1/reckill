<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>秒杀页面列表</title>
    <%@include file="common/header.jsp" %>
</head>
<body>
    <div class="container">
        <div class="panel panel-default">
            <div class="panel-heading text-center">${seckill.name}</div>
            <div class="panel-body text-center">
                <span class="glyphicon glyphicon-time "></span>
                <span class="glyphicon" id="seckill-box"></span>
            </div>
        </div>
    </div>
    <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <%--<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>--%>
                    <h3 class="modal-title text-center" id="myModalLabel"><span class="glyphicon glyphicon-phone"></span>秒杀电话</h3>
                </div>
                <div class="modal-body">
                    <div class="row">
                        <div class="col-xs-8 col-xs-offset-2">
                            <input type="text" name="killPhone" id="killPhone" placeholder="手机号" class="form-control" >
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <!--验证信息-->
                    <span id="killPhoneMessage" class="glyphicon"></span>
                    <%--<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>--%>
                    <button type="button" id="killPhoneBtn" class="btn btn-primary">
                        <span class="glyphicon glyphicon-phone"></span>
                        提交
                    </button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal -->
    </div>


</body>
<!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
<script src="http://cdn.static.runoob.com/libs/jquery/2.1.1/jquery.min.js"></script>

<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
<script src="http://cdn.static.runoob.com/libs/bootstrap/3.3.7/js/bootstrap.min.js"></script>

<script src="http://cdn.bootcss.com/jquery-cookie/1.4.1/jquery.cookie.js"></script>

<script src="http://cdn.bootcss.com/jquery.countdown/2.2.0/jquery.countdown.min.js"></script>

<script src="/resources/scripts/seckill.js" ></script>

<script type="text/javascript">
    $(function (){
        seckill.detail.init({
            seckillId : ${seckill.seckillId},
            startTime : ${seckill.startTime.time},//毫秒
            endTime : ${seckill.endTime.time}
            }
        );
    });
</script>
</html>

