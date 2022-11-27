

export var to12Format = (time) => {
    // Check correct time format and split into components
    time = time.toString ().match (/^([01]\d|2[0-3])(:)([0-5]\d)(:[0-5]\d)?$/) || [time];

    if (time.length > 1) { // If time format correct
    time = time.slice (1);  // Remove full string match value
    time[5] = +time[0] < 12 ? ' AM' : ' PM'; // Set AM/PM
    time[0] = +time[0] % 12 || 12; // Adjust hours
    }
    return time.join (''); // return adjusted time or original string
}

export var to24Format = (time) => {
    let hours = Number(time.match(/^(\d+)/)[1]);
    let minutes = Number(time.match(/:(\d+)/)[1]);
    let AMPM = time.match(/\s(.*)$/)[1];
    if(AMPM == "PM" && hours<12) hours = hours+12;
    if(AMPM == "AM" && hours==12) hours = hours-12;
    let sHours = hours.toString();
    let sMinutes = minutes.toString();
    if(hours<10) sHours = "0" + sHours;
    if(minutes<10) sMinutes = "0" + sMinutes;
    return (sHours + ":" + sMinutes);
}

export var moneyFormat =  (n)=>{
    return (Math.round(n * 100) / 100).toLocaleString() +" ₹";
}
    
export var handleApiResponse = (data)=>{
        if(data.statusCode==404){
            alert('Data not found');
        }else if(data.statusCode==500){
            alert('Please try again later');
        }else if(data.statusCode=400){
            alert('Invalid data to process');
        }else if(data.statusCode==401){
            alert("Unauthorized");
        }
}


// Validator Functions

export function isEmpty(val,element){
    let result = false;
    if(val === undefined || val === null || val.length === 0) result =  true;
    return result;
}

export function isNumber(val,element){
    let result = false;
    if(!isNaN(val)) result = true;
    return result;
}

export function isLessThanN(val,N,element) {
    let result = false;
    N = parseInt(N);
    if(isNumber(val)){
        if(parseInt(val)<N) result = true;
    }
    return result;
}

export function isGreaterThanN(val,N,element) {
    let result = false;
    N = parseInt(N);
    if(isNumber(val)){
        if(parseInt(val)>N) result = true;
    }
    return result;
}

export function isMobileNumber(val,element){
    let result = false;
    if(isNumber(val)){
        if((val+"").length==10) result = true;
    }
    return result;
}

export function isPositiveNumber(val,element){
    let result = false;

    if(isNumber(val)){
        if(parseInt(val)>=0) result = true;
    }
    return result;
}

export function isIfscCode(val,element){
    let result = false;
    var reg = /[A-Z|a-z]{4}[0][a-zA-Z0-9]{6}$/;    
    if (val.match(reg)) result = true;
    return result;
}

export function toastResponse(statusCode, success, fail){
    console.log(statusCode);
}