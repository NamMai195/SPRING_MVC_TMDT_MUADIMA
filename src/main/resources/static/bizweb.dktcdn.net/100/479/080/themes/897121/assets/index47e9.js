var is_load = 0
$(document).ready(function($) {
    !is_load && setTimeout(load_after_scroll, 10000)
    $(window).one('mousemove touchstart scroll', load_after_scroll)	
});
function load_after_scroll() {
	if (is_load) return
	is_load = 1
	var slidehome = new Swiper('.section_slider .slide-container', {
		pagination: {
			el: '.section_slider .swiper-pagination',
			clickable: true,
		},
		autoplay: {
			delay: 3000,
		},
		slidesPerView: 1,
		centeredSlides: false,
		loop: false,
		grabCursor: true,
		autoPlay:true,
		spaceBetween: 0,
		roundLengths: true,
		slideToClickedSlide: false
	});
	var service = new Swiper('.section_service', {
		slidesPerView: 5,
		autoplay: true,
		grabCursor: true,
		spaceBetween: 20,
		roundLengths: true,
		slideToClickedSlide: false,
		autoplay: true,
		breakpoints: {
			300: {
				slidesPerView: 2,
				spaceBetween: 10,
			},
			500: {
				slidesPerView: 2
			},
			640: {
				slidesPerView: 3
			},
			768: {
				slidesPerView: 3
			},
			992: {
				slidesPerView: 4
			},
			1400: {
				slidesPerView: 5
			}
		}
	});
	var swiperCarousel = new Swiper('.swiper-carousel', {
		slidesPerView: 6,
		spaceBetween: 0,
		autoplay: {
			delay: 4000
		},
		breakpoints: {
			300: {
				slidesPerView: 2
			},
			767: {
				slidesPerView: 2
			},
			768: {
				slidesPerView: 3
			},
			1024: {
				slidesPerView: 4
			},
			1200: {
				slidesPerView: 6
			}
		}
	});
	/*Ajax tab 1*/
	$(".not-dqtab").each( function(e){
		/*khai báo khởi tạo ban đầu cho 2 kiểu tab*/
		var $this1 = $(this);
		var $this2 = $(this);
		var datasection = $this1.closest('.not-dqtab').attr('data-section');
		$this1.find('.tabs-title li:first-child').addClass('current');
		$this1.find('.tab-content').first().addClass('current');
		var datasection2 = $this2.closest('.not-dqtab').attr('data-section-2');
		$this2.find('.tabs-title li:first-child').addClass('current');
		$this2.find('.tab-content').first().addClass('current');

		/*khai báo cho chức năng dành cho mobile tab*/
		var _this = $(this).find('.wrap_tab_index .title_module_main');
		var droptab = $(this).find('.link_tab_check_click');

		/*type 1*/ //kiểu 1 này thì load có owl carousel
		$this1.find('.tabtitle.ajax li').click(function(){
			var $this2 = $(this),
				tab_id = $this2.attr('data-tab'),
				url = $this2.attr('data-url');
			var etabs = $this2.closest('.e-tabs');
			etabs.find('.tab-viewall').attr('href',url);
			etabs.find('.tabs-title li').removeClass('current');
			etabs.find('.tab-content').removeClass('current');
			$this2.addClass('current');
			etabs.find("."+tab_id).addClass('current');
			//Nếu đã load rồi thì không load nữa
			if(!$this2.hasClass('has-content')){
				$this2.addClass('has-content');		
				getContentTab(url,"."+ datasection+" ."+tab_id);
			}
			$(this).parent().next().attr('href',url);
		});
	});
	// Get content cho tab
	function getContentTab(url,selector){
		url = url+"?view=ajaxload";
		var loading = '<div class="text-center"><img src="//bizweb.dktcdn.net/100/479/080/themes/897121/assets/rolling.svg?1705909380173" alt="loading"/></div>';
		$.ajax({
			type: 'GET',
			url: url,
			beforeSend: function() {
				$(selector).html(loading);
			},
			success: function(data) {
				var content = $(data);
				setTimeout(function(){
					$(selector).html(content.html());
					ajaxSwiper(selector);
					let arrImg = document.querySelector(selector).querySelectorAll('.lazyload');
					arrImg.forEach((v) => { io.observe(v);})
					$(selector + ' .add_to_cart').bind( 'click', addToCart );
					if (window.BPR !== undefined){
						return window.BPR.initDomEls(), window.BPR.loadBadges();
					}
				},500);
			},
			dataType: "html"
		});
	}
	// Ajax carousel
	function ajaxSwiper(selector,dataLgg){
		console.log(selector);
		$(selector+' .swipertab').each( function(){
			var swiperTab = new Swiper('.swipertab', {
				slidesPerView: 6,
				spaceBetween: 0,
				autoplay: {
					delay: 4000
				},
				breakpoints: {
					300: {
						slidesPerView: 2
					},
					767: {
						slidesPerView: 2
					},
					768: {
						slidesPerView: 3
					},
					1024: {
						slidesPerView: 4
					},
					1200: {
						slidesPerView: 6
					}
				}
			});
		})
	}
}