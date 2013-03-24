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


+ (NSString*) subIndexString:(NSString*) data startstr:(NSString*)startstr ;

+(NSString*)substring:(NSString*)data startstr:(NSString*)startstr endstr:(NSString*)endstr ;



+ (NSString*) substringByTag:(NSString*) data startstr:(NSString*) startstr endstr:(NSString*) endstr tagName:(NSString*) tagName;

+(NSArray*) getSubValues : (NSString*)data startstr:(NSString*)startstr endstr:(NSString*) endstr ;

+(NSArray*) getSubValuesByTag:(NSString*)data startstr:(NSString*)startstr endstr:(NSString*)endstr tagName:(NSString*)tagName ;

+(int) getCount:(NSString*) data str:(NSString*) str;

+(NSString*) getTextInTag:(NSString*) data;
@end
