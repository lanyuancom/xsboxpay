//dom加载完成后执行的js
;$(function(){
	$.ajaxSetup({   
        error: function(jqXHR, textStatus, errorThrown){  
            switch (jqXHR.status){  
                case(500):  
                    var msg = "服务器系统内部错误";  
                    break;  
                case(401):  
                    var msg = "未登录";  
                    break;  
                case(403):  
                    var msg = "无权限执行此操作";  
                    break;  
                case(408):  
                    var msg = "请求超时";  
                    break;  
                default:  
                    var msg = "未知错误";  
            }
			updateAlert(msg);
			setTimeout(function(){
				$('.Huialert').fadeOut();
			},1500);
        }
    });

    //ajax get请求
    $('.ajax-get').click(function(){
        var target;
        var that = this;
        if ( $(this).hasClass('confirm') ) {
            if(!confirm('确认要执行该操作吗?')){
                return false;
            }
        }
        if ( (target = $(this).attr('href')) || (target = $(this).attr('url')) ) {
            $.get(target).success(function(data){
                var data = eval("(" + data + ")");
				if (data.status==1) {
                    if (data.url) {
                        updateAlert(data.info + ' 页面即将自动跳转~','Huialert-success');
                    }else{
                        updateAlert(data.info,'Huialert-success');
                    }
                    setTimeout(function(){
                        if (data.url) {
                            location.href=data.url;
                        }else if( $(that).hasClass('no-refresh')){
                            $('.Huialert').fadeOut();
                        }else{
                            location.reload();
                        }
                    },1500);
                }else{
                    updateAlert(data.info);
                    setTimeout(function(){
                        if (data.url) {
                            location.href=data.url;
                        }else{
                            $('.Huialert').fadeOut();
                        }
                    },1500);
                }
            });

        }
        return false;
    });

    //ajax post submit请求
    $('.ajax-post').click(function(){
        var target,query,form;
        var target_form = $(this).attr('target-form');
        var that = this;
        var nead_confirm=false;
		
        if( ($(this).attr('type')=='submit') || (target = $(this).attr('href')) || (target = $(this).attr('url')) ){
            form = $('.'+target_form);

            if ($(this).attr('hide-data') === 'true'){//无数据时也可以使用的功能
            	form = $('.hide-data');
            	query = form.serialize();
            }else if (form.get(0)==undefined){
            	return false;
            }else if ( form.get(0).nodeName=='FORM' ){
                if ( $(this).hasClass('confirm') ) {
                    if(!confirm('确认要执行该操作吗?')){
                        return false;
                    }
                }
                if($(this).attr('url') !== undefined){
                	target = $(this).attr('url');
                }else{
					if(form.attr('action')==''){
						target = window.location.href;
					}else{
						target = form.get(0).action;
					};
                }
                query = form.serialize();
            }else if( form.get(0).nodeName=='INPUT' || form.get(0).nodeName=='SELECT' || form.get(0).nodeName=='TEXTAREA') {
                form.each(function(k,v){
                    if(v.type=='checkbox' && v.checked==true){
                        nead_confirm = true;
                    }
                })
                if ( nead_confirm && $(this).hasClass('confirm') ) {
                    if(!confirm('确认要执行该操作吗?')){
                        return false;
                    }
                }
                query = form.serialize();
            }else{
                if ( $(this).hasClass('confirm') ) {
                    if(!confirm('确认要执行该操作吗?')){
                        return false;
                    }
                }
                query = form.find('input,select,textarea').serialize();
            }
            $(that).addClass('disabled').attr('autocomplete','off').prop('disabled',true);
            $.post(target,query).success(function(data){
				var data = eval("(" + data + ")");
				if (data.status==1) {
                    if (data.url) {
                        updateAlert(data.info + ' 页面即将自动跳转~','Huialert-success');
                    }else{
                        updateAlert(data.info ,'Huialert-success');
                    }
                    setTimeout(function(){
                        if (data.url) {
                            location.href=data.url;
                        }else if( $(that).hasClass('no-refresh')){
                            $('.Huialert').fadeOut();
                            $(that).removeClass('disabled').prop('disabled',false);
                        }else{
                            location.reload();
                        }
                    },1500);
                }else{
                    updateAlert(data.info);
                    setTimeout(function(){
						$('.Huialert').fadeOut();
                        $(that).removeClass('disabled').prop('disabled',false);
                        /* if (data.url) {
                            location.href=data.url;
                        }else{
                            $('.Huialert').fadeOut();
                            $(that).removeClass('disabled').prop('disabled',false);
                        } */
                    },1500);
                }
            }).error(function(){
				setTimeout(function(){
					$(that).removeClass('disabled').prop('disabled',false);
				},1500);
			});
        }
        return false;
    });
		/**顶部警告栏*/
	window.updateAlert = function (text, c) {
		if($('.Huialert').length <= 0){
			$('.page-container').prepend('<div class="Huialert" style="display:none"><i class="icon-remove"></i>提示信息</div>');
		}
		c = c||'Huialert-error';
		$('.Huialert').html(text);
        $('.Huialert').removeClass('Huialert-error Huialert-danger Huialert-info Huialert-success').addClass(c);
		$('.Huialert').show();
	};
});

/* 上传图片预览弹出层 */
$(function(){
    $(window).resize(function(){
        var winW = $(window).width();
        var winH = $(window).height();
        
        $(".upload-img-box").click(function(){
        	//如果没有图片则不显示
        	if($(this).find('img').attr('src') === undefined){
        		return false;
        	}
            // 创建弹出框以及获取弹出图片
            var imgPopup = "<div id=\"uploadPop\" class=\"upload-img-popup\"></div>"
            var imgItem = $(this).find(".upload-pre-item").html();

            //如果弹出层存在，则不能再弹出
            var popupLen = $(".upload-img-popup").length;
            if( popupLen < 1 ) {
                $(imgPopup).appendTo("body");
                $(".upload-img-popup").html(
                    imgItem + "<a class=\"close-pop\" href=\"javascript:;\" title=\"关闭\"></a>"
                );
            }

            // 弹出层定位
            var uploadImg = $("#uploadPop").find("img");
            var popW = uploadImg.width();
            var popH = uploadImg.height();
            var left = (winW -popW)/2;
            var top = (winH - popH)/2 + 50;
            $(".upload-img-popup").css({
                "max-width" : winW * 0.9,
                "left": left,
                "top": top
            });
        });
		
        $(".upload-img-box img , .resize").on("click",function(){
        	//如果没有图片则不显示
        	//alert($(this).attr('src'));
        	if($(this).attr('src') === undefined){
        		return false;
        	}
            // 创建弹出框以及获取弹出图片
            var imgPopup = "<div id=\"uploadPop\" class=\"upload-img-popup\"></div>"
            var img = $(this).attr('src');
            var imgItem="<img src='"+img+"'>";
            //如果弹出层存在，则不能再弹出
            var popupLen = $(".upload-img-popup").length;
            if( popupLen < 1 ) {
                $(imgPopup).appendTo("body");
                $(".upload-img-popup").html(
                    imgItem + "<a class=\"close-pop\" href=\"javascript:;\" title=\"关闭\"></a>"
                );
            }

            // 弹出层定位
            var uploadImg = $("#uploadPop").find("img");
            var popW = uploadImg.width();
			var popH = uploadImg.height();
			if(popH > winH &&  popW > winW){
				if(popH/winH > popW/winW){
					popH = winW/popW*popH;
					popW = winW;
				}else{
					popW = winH/popH*popW;
					popH = winH;
				}
			}else{
				if(popW>winW){
					popH = winW/popW*popH;
					popW = winW;
				}
				if(popH>winH){
					popW = winH/popH*popW;
					popH = winH;
				}
			}
			
            var left = (winW -popW)/2;
            var top = (winH - popH)/2 + 10;
			
			
			uploadImg.css({
				"width": popW,
                "height": popH
			});
            $(".upload-img-popup").css({
                "max-width" : winW,
                "left": left,
                "top": top
            });
			$('.upload-img-popup').bind('click', function(){
				$(this).remove();
			});
        });
       
        // 关闭弹出层
        $("body").on("click", "#uploadPop .close-pop", function(){
            $(this).parent().remove();
        });
    }).resize();

    // 缩放图片
    function resizeImg(node,isSmall){
        if(!isSmall){
            $(node).height($(node).height()*1.2);
        } else {
            $(node).height($(node).height()*0.8);
        }
    }
})

$(document).ready(function(){
	$("input[type='submit']").keyup(function(event){
		if(event.keyCode===13){
			$(this).click();
		}
	});
	
	$('.Huialert').click(function(){
		$(this).hide();
	});
	
	$.Huitab("#tab-system .tabBar span","#tab-system .tabCon","current","click","0");
})