//
//  main.m
//  appletvserver
//
//  Created by zhang wei on 13-2-12.
//  Copyright (c) 2013年 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>

#import "AppDelegate.h"
#import "ffmpeg.h"
int main(int argc, char *argv[])
{
//        NSArray *path = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
//        NSString *outpath = [[path objectAtIndex:0] stringByAppendingFormat:@"%@",@"/m3u8"];
//        outpath = @"/Users/zhangwei/Desktop/m3u8";
//        if(![[NSFileManager defaultManager] fileExistsAtPath:outpath]){
//            [[NSFileManager defaultManager] createDirectoryAtPath:outpath withIntermediateDirectories:YES attributes:nil error:nil];
//        }
//    
////        NSString  *inpath=[[[NSBundle mainBundle] bundlePath] stringByAppendingPathComponent:@"1.mkv"];
//        NSString* inpath =@"http://10.0.1.8:9090/appletv/1.mkv";
//    
//        NSLog(@"OutPath:%@",outpath);
//        NSLog(@"InPath:%@",inpath);
//        NSDate* start = [NSDate date];
//        
//           convert_avi_to_m3u8([inpath cStringUsingEncoding:NSASCIIStringEncoding],[[outpath stringByAppendingString:@"/testlist.m3u8"] cStringUsingEncoding:NSASCIIStringEncoding],
//                              [[outpath stringByAppendingString:@"/stream%05d.ts"] cStringUsingEncoding:NSASCIIStringEncoding]);
//    
////    convert_avi_to_mp4([inpath cStringUsingEncoding:NSASCIIStringEncoding],[[outpath stringByAppendingString:@"/test.mp4"] cStringUsingEncoding:NSASCIIStringEncoding]);
//    
//        NSLog(@"Start Date:%@",[start description]);
//        NSLog(@"End Date:%@",[[NSDate date] description]);
        NSAutoreleasePool *pool = [[NSAutoreleasePool alloc] init];
        int retVal =UIApplicationMain(argc, argv, nil, NSStringFromClass([AppDelegate class]));
        [pool release];
        return retVal;
 
}
