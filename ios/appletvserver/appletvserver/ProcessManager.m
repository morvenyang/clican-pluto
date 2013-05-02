//
//  ProcessManager.m
//  appletvserver
//
//  Created by zhang wei on 13-4-12.
//
//

#import "ProcessManager.h"
#import "AppDelegate.h"
#import "ffmpeg.h"

@implementation ProcessManager

+(void) changeRunningProcess:(NSString*) process{
    if([process isEqualToString:@"m3u8"]){
        AppDele.m3u8Process.running = YES;
        AppDele.mp4Process.running = NO;
        AppDele.mp4Process.mp4Url = nil;
        AppDele.mkvProcess.mkvUrl = nil;
        transfer_code_interrupt = 1;
    }else if([process isEqualToString:@"mp4"]){
        AppDele.mp4Process.running =YES;
        AppDele.m3u8Process.running = NO;
        AppDele.m3u8Process.m3u8Url = nil;
        AppDele.mkvProcess.mkvUrl = nil;
        transfer_code_interrupt = 1;
    }else if([process isEqualToString:@"mkv"]){
        transfer_code_interrupt = 0;
        AppDele.m3u8Process.running = NO;
        AppDele.mp4Process.running = NO;
        AppDele.mp4Process.mp4Url = nil;
        AppDele.m3u8Process.m3u8Url = nil;
    }
}

+(void) stopMkv{
    transfer_code_interrupt = 1;
}
@end
