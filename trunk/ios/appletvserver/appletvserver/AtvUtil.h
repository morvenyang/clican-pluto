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
+ (BOOL) content:(NSString *)content startWith:(NSString*) prefix;
+ (NSString*) md5:(NSData*) data;
+ (NSData *)uncompressZippedData:(NSData *)compressedData;
+ (NSString*) getUUID;
+ (BOOL) isWifi;
@end
