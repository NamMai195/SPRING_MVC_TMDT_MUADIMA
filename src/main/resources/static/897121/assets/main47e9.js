const io = new IntersectionObserver((entries, imgObserver) => {
	entries.forEach((entry) => {
		if (entry.isIntersecting) {
			entry.target.src = entry.target.dataset.src;
			entry.target.classList.add('loaded');
			imgObserver.unobserve(entry.target);
		}
	})
});
const bo = new IntersectionObserver((entries, imgObserver) => {
	entries.forEach((entry) => {
		if (entry.isIntersecting) {
			const lazyBackgorundImage = entry.target;
			lazyBackgorundImage.style.backgroundImage = lazyBackgorundImage.dataset.background;
			entry.target.classList.add('loaded');
			imgObserver.unobserve(entry.target);
		}
	})
});
document.addEventListener("DOMContentLoaded", function() {
	const arr = document.querySelectorAll('.lazyload')
	arr.forEach((v) => {
		io.observe(v);
	})
	const arrBg = document.querySelectorAll('.lazyload_bg')
	arrBg.forEach((v) => {
		bo.observe(v);
	})
})
var is_renderd = 0;
$(document).ready(function ($) {
	$(window).one('mousemove touchstart load',renderLayout );
	//Sửa lỗi phải click 2 lần
	$('a').bind('touchend', function() {});
});
var wDWs = $(window).width();
function renderLayout(){
	if(is_renderd) return 
	is_renderd = 1
	//<![CDATA[ 
	function loadCSS(e, t, n) { "use strict"; var i = window.document.createElement("link"); var o = t || window.document.getElementsByTagName("footer")[0]; i.rel = "stylesheet"; i.href = e; i.media = "only x"; o.parentNode.insertBefore(i, o); setTimeout(function () { i.media = n || "all" }) }
	loadCSS("https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.1/css/all.min.css");
	//]]> 
	if(localStorage.last_viewed_products != undefined) {
		var last_viewd_pro_array = JSON.parse(localStorage.last_viewed_products);
		if(document.querySelector('.countviewed') != null) {
			document.querySelector('.countviewed').innerHTML = last_viewd_pro_array.length;
		}
	}
	var placeholderText = ['Bạn muốn tìm gì?','iPhone', 'Tivi', 'Mỹ phẩm', 'Đồ gia dụng','Laptop','Trang sức','Túi ví','...'];
	$('.search-auto').placeholderTypewriter({text: placeholderText});
	awe_backtotop();
	awe_category();
	search_smart();
	if (document.querySelector('.qtyplus') != null) {
		document.querySelector('.qtyplus').addEventListener('click', function(e){
			e.preventDefault();   
			fieldName = this.dataset.field; 
			var numberA = document.querySelector('input[data-field='+fieldName+']').value();
			var currentVal = parseInt(numberA);
			if (!isNaN(currentVal)) { 
				document.querySelector('input[data-field='+fieldName+']').value = currentVal + 1;
			} else {
				document.querySelector('input[data-field='+fieldName+']').value = 0;
			}
		});
	}
	if (document.querySelector('.qtyminus') != null) {
		document.querySelector('.qtyminus').addEventListener('click', function(e){
			e.preventDefault(); 
			fieldName = this.dataset.field; 
			var numberA = document.querySelector('input[data-field='+fieldName+']').value();
			var currentVal = parseInt(numberA);
			if (!isNaN(currentVal) && currentVal > 1) {          
				document.querySelector('input[data-field='+fieldName+']').value = currentVal - 1;
			} else {
				document.querySelector('input[data-field='+fieldName+']').value = 1;
			}
		});
	}
	if (wDWs < 1199 && wDWs > 992) {
		$('.item_big li:has(ul)' ).one("click", function(e)     {
			e.preventDefault();
			return false;    
		});
		$('.ul_menu li:has(ul)' ).one("click", function(e)     {
			e.preventDefault();
			return false;    
		});
	}
	if (wDWs < 1199 && wDWs > 992) {
		$('.ul_menu li:has(ul)' ).one("click", function(e) {
			e.preventDefault();
			return false;    
		});
	}
	$('.ul_menu li .fa').click(function(e){
		if($(this).hasClass('current')) {
			$(this).closest('.ul_menu').find('li, .fa').removeClass('current');
		} else {
			$(this).closest('.ul_menu').find('li, .fa').removeClass('current');
			$(this).closest('li').addClass('current');
			$(this).addClass('current');
		}
	});
	$('.item_big li .fa').click(function(e){
		if($(this).hasClass('current')) {
			$(this).closest('ul').find('li, .fa').removeClass('current');
		} else {
			$(this).closest('ul').find('li, .fa').removeClass('current');
			$(this).closest('li').addClass('current');
			$(this).addClass('current');
		}
	});
	var opacityEvent = document.querySelectorAll('.opacity_menu'),
		closeMenu = document.querySelectorAll('.menu-main .menu-title'),
		categoryNav = document.querySelectorAll('.menu_bar');
	opacityEvent.forEach(function(el){
		el.addEventListener('click', function () {
			document.querySelector('html').style.overflow = "initial";
			document.querySelector('.menu-main').classList.remove('current');
			document.querySelector('.opacity_menu').classList.remove('current');
			if(document.querySelector('.dqdt-sidebar, .open-filters, .open-filters')!= null) {
				document.querySelector('.dqdt-sidebar, .open-filters').classList.remove('openf');
				document.querySelector('.open-filters').classList.remove('openf');
				document.querySelector('.open-filters').style.display = "inline-block";
			}
		});
	});
	closeMenu.forEach(function(el){
		el.addEventListener('click', function () {
			document.querySelector('html').style.overflow = "initial";
			document.querySelector('.menu-main').classList.remove('current');
			document.querySelector('.opacity_menu').classList.remove('current');
			if(document.querySelector('.dqdt-sidebar, .open-filters, .open-filters')!= null) {
				document.querySelector('.dqdt-sidebar, .open-filters').classList.remove('openf');
				document.querySelector('.open-filters').classList.remove('openf');
				document.querySelector('.open-filters').style.display = "inline-block";
			}
		});
	});
	categoryNav.forEach(function(el){
		el.addEventListener('click', function () {
			document.querySelector('html').style.overflow = "hidden";
			document.querySelector('.menu-main').classList.add('current');
			document.querySelector('.opacity_menu').classList.toggle('current');
			if(document.querySelector('.dqdt-sidebar, .open-filters')!= null) {
				document.querySelector('.open-filters').style.display = "none";
				document.querySelector('.open-filters').classList.remove('openf');
				document.querySelector('.dqdt-sidebar, .open-filters').classList.remove('openf');
			}
		});
	});
	if (wDWs < 991) {
		var openFilter = document.querySelectorAll('.open-filters');

		openFilter.forEach(function(el){
			el.addEventListener('click', function () {
				this.classList.toggle('openf');
				document.querySelector('.dqdt-sidebar').classList.toggle('openf');
				document.querySelector('.opacity_menu').classList.toggle('current');
			});
		});
	}
	if (wDWs < 768){
		$(document).on('click', '.title-menu', function(){
			$(this).toggleClass('active');
		})
	}
}
function awe_convertVietnamese(str) { 
	str= str.toLowerCase();
	str= str.replace(/à|á|ạ|ả|ã|â|ầ|ấ|ậ|ẩ|ẫ|ă|ằ|ắ|ặ|ẳ|ẵ/g,"a"); 
	str= str.replace(/è|é|ẹ|ẻ|ẽ|ê|ề|ế|ệ|ể|ễ/g,"e"); 
	str= str.replace(/ì|í|ị|ỉ|ĩ/g,"i"); 
	str= str.replace(/ò|ó|ọ|ỏ|õ|ô|ồ|ố|ộ|ổ|ỗ|ơ|ờ|ớ|ợ|ở|ỡ/g,"o"); 
	str= str.replace(/ù|ú|ụ|ủ|ũ|ư|ừ|ứ|ự|ử|ữ/g,"u"); 
	str= str.replace(/ỳ|ý|ỵ|ỷ|ỹ/g,"y"); 
	str= str.replace(/đ/g,"d"); 
	str= str.replace(/!|@|%|\^|\*|\(|\)|\+|\=|\<|\>|\?|\/|,|\.|\:|\;|\'| |\"|\&|\#|\[|\]|~|$|_/g,"-");
	str= str.replace(/-+-/g,"-");
	str= str.replace(/^\-+|\-+$/g,""); 
	return str; 
} window.awe_convertVietnamese=awe_convertVietnamese;
function awe_category(){
	if (document.querySelectorAll('.nav-category .fa-plus') != undefined) {
		var faplus = document.querySelectorAll('.nav-category .fa-plus'),
			faminus = document.querySelectorAll('.nav-category .fa-minus');
		faplus.forEach(function(fap) {
			fap.addEventListener('click', function () { 
				fap.classList.toggle('fa-minus');
				fap.parentNode.classList.toggle('active');
			})
		})
		faminus.forEach(function(fam) {
			fam.addEventListener('click', function () { 
				fam.classList.toggle('fa-plus');
				fam.parentNode.classList.toggle('active');
			})
		})
	}
} window.awe_category=awe_category;
function awe_backtotop() { 
	window.onscroll = function() {scrollFunction()};
	function scrollFunction() {
		if (document.body.scrollTop > 200 || document.documentElement.scrollTop > 200) {
			document.querySelector('.backtop').classList.add('show');
		} else {
			document.querySelector('.backtop').classList.remove('show');
		}
	}
	document.querySelector('.backtop').addEventListener("click", function(){
		scrollTo(0, 300);
	});
	function scrollTo(element, duration) {
		var e = document.documentElement;
		if (e.scrollTop === 0) {
			var t = e.scrollTop;
			++e.scrollTop;
			e = t + 1 === e.scrollTop-- ? e : document.body;
		}
		scrollToC(e, e.scrollTop, element, duration);
	}
	function scrollToC(element, from, to, duration) {
		if (duration <= 0) return;
		if (typeof from === "object") from = from.offsetTop;
		if (typeof to === "object") to = to.offsetTop;

		scrollToX(element, from, to, 0, 1 / duration, 20, easeOutCuaic);
	}

	function scrollToX(element, xFrom, xTo, t01, speed, step, motion) {
		if (t01 < 0 || t01 > 1 || speed <= 0) {
			element.scrollTop = xTo;
			return;
		}
		element.scrollTop = xFrom - (xFrom - xTo) * motion(t01);
		t01 += speed * step;
		setTimeout(function() {
			scrollToX(element, xFrom, xTo, t01, speed, step, motion);
		}, step);
	}

	function easeOutCuaic(t) {
		t--;
		return t * t * t + 1;
	}

} window.awe_backtotop=awe_backtotop;
function search_smart(){
	$('.evo_sidebar_search .evo-search-form input[type="text"]').bind('focusin focusout', function(e){
		$(this).closest('.evo-search').toggleClass('focus', e.type == 'focusin');
	});
	var preLoadLoadGif = $('<div class="spinner-border text-primary" role="status"><span class="sr-only">Loading...</span></div>');
	var searchTimeoutThrottle = 500;
	var searchTimeoutID = -1;
	var currReqObj = null;
	var $resultsBox = $('<div class="results-box" />').appendTo('.search-form');
	$('.search-form input[type="text"]').bind('keyup change', function(){
		if($(this).val().length > 2 && $(this).val() != $(this).data('oldval')) {
			$(this).data('oldval', $(this).val());
			if(currReqObj != null) currReqObj.abort();
			clearTimeout(searchTimeoutID);
			var $form = $(this).closest('form');
			var term = '*' + $form.find('input[name="query"]').val() + '*';
			var linkURL = $form.attr('action') + '?query=' + term + '&type=product';
			$resultsBox.html('<div class="evo-loading"><div class="spinner-border text-primary" role="status"><span class="sr-only">Loading...</span></div></div>');
			searchTimeoutID = setTimeout(function(){
				currReqObj = $.ajax({
					url: $form.attr('action'),
					async: false,
					data: {
						type: 'product',
						view: 'json',
						q: term
					},
					dataType: 'json',
					success: function(data){
						currReqObj = null;
						if(data.results_total == 0) {
							$resultsBox.html('<div class="note">Không có kết quả tìm kiếm</div>');
						} else {
							$resultsBox.empty();
							$.each(data.results, function(index, item){
								var xshow = 'wholesale';
								if(!(item.title.toLowerCase().indexOf(xshow) > -1)) {
									var $row = $('<a class="clearfix"></a>').attr('href', item.url).attr('title', item.title);
									$row.append('<div class="img"><img src="' + item.thumb + '" /></div>');
									$row.append('<div class="d-title">'+item.title+'</div>');
									$row.append('<div class="d-title d-price">'+item.price+'</div>');
									$resultsBox.append($row);
								}
							});
							$resultsBox.append('<a href="' + linkURL + '" class="note" title="Xem tất cả">Xem tất cả</a>');
						}
					}
				});
			}, searchTimeoutThrottle);
		} else if ($(this).val().length <= 2) {
			$resultsBox.empty();
		}
	}).attr('autocomplete', 'off').data('oldval', '').bind('focusin', function(){
		$resultsBox.fadeIn(200);
	}).bind('click', function(e){
		e.stopPropagation();
	});
	$('body').bind('click', function(){
		$resultsBox.fadeOut(200);
	});
} window.search_smart=search_smart;
$('.addThis_iconContact .item-contact,.addThis_listSharing .addThis_close').on('click', function(e){
	if($('.addThis_listSharing').hasClass('active')){
		$('.addThis_listSharing').removeClass('active');
		$('.addThis_listSharing').fadeOut(150);				
	}
	else{		
		$('.addThis_listSharing').fadeIn(100);
		$('.addThis_listSharing').addClass('active');
	}
});