function placeholders2() {
	$('.placeholder [type=text], .placeholder [type=password], .placeholder [type=email], .placeholder [type=number], .placeholder [type=tel], .placeholder [type=search]').each(function(index) {
        if (!$(this).parent().hasClass('ac-placeholder')){
            if ($(this).val()) {
                $(this).addClass('focus');
            }

            var _acPH = $(this).attr('placeholder');
            $(this).removeAttr('placeholder').attr('ac-placeholder', _acPH).wrap("<div class='ac-placeholder'></div>").after('<label>' + _acPH + '</label>');
        };
	});

	$('.ac-placeholder input, .ac-placeholder select, .ac-placeholder textarea').focus(function() {
			$(this).parent().addClass('focus');
	});

	$('.ac-placeholder label').click(function() {
			$(this).parent().addClass('focus');
			$(this).prev().focus();
	});

	$(".ac-placeholder input, .ac-placeholder select, .ac-placeholder textarea").blur(function() {
			if ($(this).val()) {
					$(this).parent().addClass('text');
			} else {
					$(this).parent().removeClass('text');
			}

            if ($(this).context.type == 'tel') {
                
                var count=0;var tmp="";
                var re =/\D+/g;
                val = $(this).val();
                val = val.replace( re,"");

                if(val.length>10){
                    re = /^00/;
                 //   <!-- 00 por el + previo al codigo del pais-->
                    val = val.replace( re,"");
               //         <!-- 54/549 se cargaria el codigo del pais-->
                    if(val.substring(0,3)=="549"){
                        re = /^549/;
                    }else if(val.substring(0,2)=="54"){
                        re = /^54/;
                    }
                    val = val.replace( re,"");
                    re = /^0/;
                 //   <!-- se cargaria del inicial del codigo de la ciudad-->
                    val = val.replace( re,"");
                }
                if(val.length==12){
                    re = /15/;
                    if(val.substring(1,3)=="15"){
                        val = val.replace( re,"");
                    }else if(val.substring(2,4)=="15"){
                        val = val.replace( re,"");
                    }else if(val.substring(3,5)=="15"){
                        val = val.replace( re,"");
                    }
                }
            }
        
			$(this).parent().removeClass('focus');
	});
};

placeholders2();