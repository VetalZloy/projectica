var pattern = "You will be redirected on login page in ";

$( document ).ready(function() {
	delayedLoop(5);
});

function delayedLoop(i) {
	if(i == 0){
		location.href = location.href.replace(/registrationConfirm.*/, '?login')
	}
	else {
		i--;
		setTimeout(() => {
			$('.additions').text(pattern + i + "s");
			delayedLoop(i);
		}, 1000);
	}
}