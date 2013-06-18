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
#import <CommonCrypto/CommonDigest.h>
#import "zlib.h"
#import "Reachability.h"
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
                NSString *name = [NSString stringWithUTF8String:temp_addr->ifa_name];
                if ([name isEqualToString:@"en0"]) { // Wi-Fi adapter
                    address =[NSString stringWithUTF8String:inet_ntoa(((struct sockaddr_in *)temp_addr->ifa_addr)->sin_addr)];
                }
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
    if(string==nil){
        return nil;
    }
	NSString *newString = [NSMakeCollectable(CFURLCreateStringByAddingPercentEscapes(kCFAllocatorDefault, (CFStringRef)string, NULL, CFSTR(":/?#[]@!$ &'()*+,;=\"<>%{}|\\^~`"), CFStringConvertNSStringEncodingToEncoding(NSUTF8StringEncoding))) autorelease];
	if (newString) {
		return newString;
	}
	return @"";
}

+ (BOOL) content:(NSString *)content contains:(NSString*) contains{
    if(content==nil){
        return false;
    }
    NSRange range=[[content uppercaseString] rangeOfString:[contains uppercaseString]];
    return range.location!=NSNotFound;
}

+ (BOOL) content:(NSString *)content startWith:(NSString*) prefix{
    if(content==nil){
        return false;
    }
    NSRange range=[[content uppercaseString] rangeOfString:[prefix uppercaseString]];
    return range.location==0;
}

+ (NSString*) md5:(NSData*) data{
    unsigned int outputLength = CC_MD5_DIGEST_LENGTH;
    unsigned char output[outputLength];
    CC_MD5(data.bytes, (unsigned int) [data length], output);
    NSLog(@"%s",output);
    NSMutableString* hash = [NSMutableString stringWithCapacity:outputLength * 2];
	for (unsigned int i = 0; i < outputLength; i++) {
		[hash appendFormat:@"%02x", output[i]];
	}
	return hash;
}

+(NSData *)uncompressZippedData:(NSData *)compressedData

{
    
    
    
    if ([compressedData length] == 0) return compressedData;
    
    
    
    unsigned full_length = [compressedData length];
    
    
    
    unsigned half_length = [compressedData length] / 2;
    
    NSMutableData *decompressed = [NSMutableData dataWithLength: full_length + half_length];
    
    BOOL done = NO;
    
    int status;
    
    z_stream strm;
    
    strm.next_in = (Bytef *)[compressedData bytes];
    
    strm.avail_in = [compressedData length];
    
    strm.total_out = 0;
    
    strm.zalloc = Z_NULL;
    
    strm.zfree = Z_NULL;
    
    if (inflateInit2(&strm, (15+32)) != Z_OK) return nil;
    
    while (!done) {
        
        // Make sure we have enough room and reset the lengths.
        
        if (strm.total_out >= [decompressed length]) {
            
            [decompressed increaseLengthBy: half_length];
            
        }
        
        strm.next_out = [decompressed mutableBytes] + strm.total_out;
        
        strm.avail_out = [decompressed length] - strm.total_out;
        
        // Inflate another chunk.
        
        status = inflate (&strm, Z_SYNC_FLUSH);
        
        if (status == Z_STREAM_END) {
            
            done = YES;
            
        } else if (status != Z_OK) {
            
            break;
            
        }
        
        
        
    }
    
    if (inflateEnd (&strm) != Z_OK) return nil;
    
    // Set real length.  
    
    if (done) {  
        
        [decompressed setLength: strm.total_out];  
        
        return [NSData dataWithData: decompressed];  
        
    } else {  
        
        return nil;  
        
    }  
    
}

+ (NSString*) getUUID{
    CFUUIDRef theUUID = CFUUIDCreate(NULL);
    CFStringRef string = CFUUIDCreateString(NULL, theUUID);
    CFRelease(theUUID);
    return [(NSString*) string autorelease];
}

+(BOOL)isWifi{
    Reachability *reachability = [Reachability reachabilityWithHostName:@"www.sina.com.cn"];
    NetworkStatus remoteHostStatus = [reachability currentReachabilityStatus];
    if (remoteHostStatus == kReachableViaWiFi){
        return YES;
    }else{
        return NO;
    }
}

+(unsigned long long) getFolderSize:(NSString *)filePath{
    NSFileManager *fileManager = [NSFileManager defaultManager];
    unsigned long long folderSize = 0;
    NSString* previousOne = nil;
    if ([fileManager fileExistsAtPath:filePath]) {
        NSEnumerator *childEnumber = [[fileManager subpathsOfDirectoryAtPath:filePath error:nil] objectEnumerator];
        NSString *fileName;
        while ((fileName = [childEnumber nextObject]) != nil) {
            if(previousOne!=nil){
                if([AtvUtil content:fileName startWith:previousOne]){
                    NSLog(@"skip this file %@",fileName);
                    continue;
                }
            }
            NSString *childFilePath = [filePath stringByAppendingPathComponent:fileName];
            folderSize += [fileManager attributesOfItemAtPath:childFilePath error:nil].fileSize;
        }
    }
    return folderSize;
}
@end
