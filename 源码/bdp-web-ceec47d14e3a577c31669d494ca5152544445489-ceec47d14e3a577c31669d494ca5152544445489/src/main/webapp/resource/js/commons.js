


function regexNum(str) {
	var regex = /(\d)(?=(\d\d\d)+(?!\d))/g;
	if (str.indexOf(".") == -1) {
		str = str.replace(regex, ',') + '.00';
		console.log(str)
	} else {
		var newStr = str.split('.');
		var str_2 = newStr[0].replace(regex, ',');
		if (newStr[1].length <= 1) {
			//小数点后只有一位时
			str_2 = str_2 + '.' + newStr[1] + '0';
			console.log(str_2)
		} else if (newStr[1].length > 1) {
			//小数点后两位以上时
			var decimals = newStr[1].substr(0, 2);
			var srt_3 = str_2 + '.' + decimals;
			console.log(srt_3)
		}
	}
}