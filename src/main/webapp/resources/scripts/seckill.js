var seckill={
    //封装秒杀相关ajax的url
    URL : {
        now : function () {
            return '/seckill/time/now';
        },
        exposer : function (seckillId) {
            return '/seckill/'+seckillId+"/exposer";
        },
        execution : function (seckillId,md5) {
            return "/seckill/"+seckillId+"/"+md5+'/execution';
        }
    },
    handleSeckillkill:function(seckillId,node){
        node.hide().html("<button class='btn btn-primary btn-lg' id='killBtn'>开始秒杀</button>");
        $.post(seckill.URL.exposer(seckillId),{},function (result) {
            //在回掉过程中，执行交互过程
            if(result && result['success']){
                var exposer = result['data'];
                if(exposer['exposed']){
                    //开启秒杀
                    //获取秒杀地址
                    var md5 = exposer['md5'];
                    var killUrl = seckill.URL.execution(seckillId,md5);
                    console.log("killUrl:",killUrl);
                    $("#killBtn").one('click',function () {
                       //绑定点击事件执行秒杀操作
                        //先禁用按钮
                        $(this).addClass("disabled");
                        //发送请求
                        $.post(killUrl,{},function (result) {
                            if(result){
                                var killResult = result['data'];
                                var state = killResult['state'];
                                var stateInfo = killResult['stateInfo'];
                                node.html("<span class='label label-success'>"+stateInfo+"</span>");
                            }
                        });
                    });
                    node.show();
                }else {
                    var now = exposer['now'];
                    var startTime = exposer['start'];
                    var endTime = exposer['end'];
                    //重新进入计时面板
                    seckill.countdown(seckillId,now,startTime,endTime);
                }
            }
        });
    },
    validatePhone:function (phone) {
      if(phone && phone.length == 11 && !isNaN(phone)){
          return true;
      }
      return false;
    },
    countdown:function(seckillId,nowTime,startTime,endTime){
        var seckillBox=$("#seckill-box");
        if(nowTime>endTime){
            seckillBox.html("秒杀结束");
        }else if(nowTime < startTime) {
            var killTime = new Date(startTime+1000);
            seckillBox.countdown(killTime,function (event) {
                var format = event.strftime("秒杀倒计时：%D天 %H时 %M分 %S秒");
                seckillBox.html(format);
            }).on("finish.countdown",function () {
                //获取秒杀地址，控制实现逻辑，执行秒杀
                seckill.handleSeckillkill(seckillId,seckillBox);
            });
        }else {
            //秒杀开始
            seckill.handleSeckillkill(seckillId,seckillBox);

        }
    },
    //详情页秒杀逻辑
    detail : {
        //详情页初始化
        init : function (params) {
            //手机验证和登录，计时交互
            //规划我们的交互流程
            var killPhone = $.cookie("killPhone");
            var startTime = params.startTime;
            var startTime = params["startTime"];
            var endTime = params["endTime"];
            var seckillId = params["seckillId"]
            //验证手机号
            if(!seckill.validatePhone(killPhone)){
                //绑定phone
                var myModal = $("#myModal");
                myModal.modal({
                    show : true,
                    backdrop : 'static',
                    keyboard : false
                });
                var killPhoneBtn = $("#killPhoneBtn").click(function () {
                    var inputPhone = $("#killPhone").val();
                    if(seckill.validatePhone(inputPhone)){
                        $.cookie("killPhone",inputPhone,{expires:7,path:'/seckill'});//有效期七天，只在seckill路径下有效
                        window.location.reload();
                    }else {
                        var killPhoneMessage = $("#killPhoneMessage");
                        killPhoneMessage.hide().html('<label class="label label-danger">手机号码错误</label>').show(300);
                    }
                });
            }

            //已经登录  计时交互
            $.get(seckill.URL.now(),{},function (result) {
                if(result && result['success']){
                    var nowTime = result["data"];
                    //时间判断
                    seckill.countdown(seckillId,nowTime,startTime,endTime);
                }else {
                    console.log("result:"+result);
                }
            });
        }
    }
}