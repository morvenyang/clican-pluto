//
//  AtvUtil.h
//  appletvserver
//
//  Created by zhang wei on 13-2-13.
//  Copyright (c) 2013年 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface AtvUtil : NSObject{
}
+(void)markReflect:(CALayer*)mainLayer image:(UIImage*)img;
+ (NSString*) getIPAddress;
+ (NSString*) encodeURL:(NSString *)string;
+ (BOOL) content:(NSString *)content contains:(NSString*) contains;

@end
