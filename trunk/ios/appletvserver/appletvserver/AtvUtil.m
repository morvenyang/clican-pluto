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
@end
