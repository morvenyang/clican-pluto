//
//  AtvUtil.m
//  appletvserver
//
//  Created by zhang wei on 13-2-13.
//  Copyright (c) 2013年 __MyCompanyName__. All rights reserved.
//

#import "AtvUtil.h"
#include <ifaddrs.h>
#include <arpa/inet.h>

@implementation AtvUtil

+(void)markReflect:(CALayer*)mainLayer image:(UIImage*)img {
    CALayer* imgLayer=[CALayer layer];
	imgLayer.frame=CGRectMake(0, 0, 93,124);
   
	imgLayer.contents=(id)img.CGImage;
    imgLayer.masksToBounds =YES;
    imgLayer.cornerRadius= 8;
	[mainLayer addSublayer:imgLayer];
    
	CALayer* reflectionLayer=[CALayer layer];
	reflectionLayer.contents=imgLayer.contents;
	reflectionLayer.opacity = 0.3;
	reflectionLayer.frame=CGRectMake(0, 124, 93, 40);
    
    reflectionLayer.masksToBounds =YES;
    reflectionLayer.cornerRadius= 8;
    reflectionLayer.transform = CATransform3DMakeScale(1.0, -1.0, 1.0); 
	reflectionLayer.sublayerTransform = reflectionLayer.transform;
	[mainLayer addSublayer:reflectionLayer];
	
    
	CALayer* shadowLayer = [CALayer layer];
    
	shadowLayer.contents=(id)img.CGImage;
	shadowLayer.frame=reflectionLayer.bounds;
    shadowLayer.masksToBounds =YES;
    shadowLayer.cornerRadius= 8;
	reflectionLayer.mask=shadowLayer;
    
}

+ (NSString*) getIPAddress
{
    NSString *address = @"error";
    struct ifaddrs *interfaces = NULL;
    struct ifaddrs *temp_addr = NULL;
    int success = 0;
    
    // retrieve the current interfaces - returns 0 on success
    success = getifaddrs(&interfaces);
    if (success == 0) {
        // Loop through linked list of interfaces
        temp_addr = interfaces;
        while (temp_addr != NULL) {
            if( temp_addr->ifa_addr->sa_family == AF_INET) {
                address = [NSString stringWithUTF8String:inet_ntoa(((struct sockaddr_in *)temp_addr->ifa_addr)->sin_addr)];
            }
            temp_addr = temp_addr->ifa_next;
        }
    }
    
    // Free memory
    freeifaddrs(interfaces);
    
    return address;
}

+ (NSString*)encodeURL:(NSString *)string
{
	NSString *newString = [NSMakeCollectable(CFURLCreateStringByAddingPercentEscapes(kCFAllocatorDefault, (CFStringRef)string, NULL, CFSTR(":/?#[]@!$ &'()*+,;=\"<>%{}|\\^~`"), CFStringConvertNSStringEncodingToEncoding(NSUTF8StringEncoding))) autorelease];
	if (newString) {
		return newString;
	}
	return @"";
}


+ (NSString*) subIndexString:(NSString*) data startstr:(NSString*)startstr {
    NSRange range = [data rangeOfString:startstr];
    if(range.location==NSNotFound){
        return @"";
    }else{
        return [data substringFromIndex:range.location+range.length];
    }
}

+(NSString*)substring:(NSString*)data startstr:(NSString*)startstr endstr:(NSString*)endstr {
    NSRange srange = [data rangeOfString:startstr];
    if(srange.location==NSNotFound){
        return @"";
    }
    NSRange erange = [data rangeOfString:endstr options:nil range:NSMakeRange(srange.location+srange.length, [data length]-srange.length-srange.location)];
    if(erange.location==NSNotFound){
        return @"";
    }
    return [data substringWithRange:NSMakeRange(srange.location+srange.length, erange.location)];
}



+ (NSString*) substringByTag:(NSString*) data startstr:(NSString*) startstr endstr:(NSString*) endstr tagName:(NSString*) tagName {
    NSRange srange = [data rangeOfString:startstr];
    if(srange.location==NSNotFound){
        return @"";
    }
    int offset = srange.location+srange.length;
    while(true){
        NSRange erange = [data rangeOfString:endstr options:nil range:NSMakeRange(offset, [data length]-offset)];
        if(erange.location==NSNotFound){
            return @"";
        }
        NSString* temp = [data substringWithRange:NSMakeRange(srange.location+srange.length, erange.location-srange.location-srange.length)];
        
        int a = [AtvUtil getCount:temp str:[NSString stringWithFormat:@"<%@",tagName]];
        int b = [AtvUtil getCount:temp str:[NSString stringWithFormat:@"</%@",tagName]];
        if(a==b){
            return temp;
        }else{
            offset = erange.location+erange.length;
        }
    }
    return @"";
}

+(NSArray*) getSubValues : (NSString*)data startstr:(NSString*)startstr endstr:(NSString*) endstr {
    NSMutableArray* values=[NSMutableArray array];
    int start = 0;
    int end = 0;
    while(start>=0){
        NSRange srange = [data rangeOfString:startstr options:nil range:NSMakeRange(start, [data length]-start)];
        start = srange.location;
        if(start==NSNotFound){
            break;
        }
        NSRange erange = [data rangeOfString:endstr options:nil range:NSMakeRange(srange.location+srange.length, [data length]-srange.length-srange.location)];
        end = erange.location;
        if(end==NSNotFound){
            break;
        }
        NSString* temp = [data substringWithRange:NSMakeRange(srange.location+srange.length, erange.location-srange.location-srange.length)];
        [values addObject:temp];
        start = end+erange.length;
    }
    return values;
}

+(NSArray*) getSubValuesByTag:(NSString*)data startstr:(NSString*)startstr endstr:(NSString*)endstr tagName:(NSString*)tagName {
    NSMutableArray* values=[NSMutableArray array];
    int start = 0;
    int end = 0;
    while(start>=0){
        NSRange srange = [data rangeOfString:startstr options:nil range:NSMakeRange(start, [data length]-start)];
        start = srange.location;
        if(start==NSNotFound){
            break;
        }
        int offset = srange.location+srange.length;
        while(true){
            NSRange erange = [data rangeOfString:endstr options:nil range:NSMakeRange(offset, [data length]-offset)];
            end = erange.location;
            if(end==NSNotFound){
                break;
            }
            NSString* temp = [data substringWithRange:NSMakeRange(offset, erange.location)];
            
            
            int a = [AtvUtil getCount:temp str:[NSString stringWithFormat:@"<%@",tagName]];
            int b = [AtvUtil getCount:temp str:[NSString stringWithFormat:@"</%@",tagName]];
           
            
            if(a==b){
                [values addObject:temp];
                break;
            }else{
                offset = erange.location+erange.length;
            }
        }
        start = end+endstr.length;
    }
    return values;
}

+(int) getCount:(NSString*) data str:(NSString*) str{
    int count = 0;
    int offset = 0;
    while(offset>=0){
        NSRange range = [data rangeOfString:str];
        offset = range.location;
        if(offset>=0){
            count++;
            offset=offset+range.length;
        }
    }
    return count;
}

+(NSString*) getTextInTag:(NSString*) data{
    int left = 0;
    int right = 0;
    int rightIndex=0;
    NSString* result = @"";
    for(int i=0;i<data.length;i++){
        unichar ch = [data characterAtIndex:i];
        if(ch=='<'){
            left++;
        }else if(ch=='>'){
            right++;
            rightIndex = i;
        }else{
            if(left==right){
                result = [result stringByAppendingString:[data substringWithRange:NSMakeRange(rightIndex+1, i-rightIndex)]];
            }
        }
    }
    return result;
}
@end
