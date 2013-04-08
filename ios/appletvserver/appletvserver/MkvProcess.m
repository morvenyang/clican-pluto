//
//  MkvProcess.m
//  appletvserver
//
//  Created by zhang wei on 13-4-5.
//
//

#import "MkvProcess.h"
#import "AppDelegate.h"
#import "ffmpeg.h"

@implementation MkvProcess

@synthesize m3u8Url = _m3u8Url;
@synthesize mkvUrl = _mkvUrl;
@synthesize running = _running;
@synthesize mkvM3u8Content = _mkvM3u8Content;
-(void) dealloc{
    TT_RELEASE_SAFELY(_m3u8Url);
    TT_RELEASE_SAFELY(_mkvUrl);
    [super dealloc];
}

-(void) convertToM3u8MkvUrl:(NSString*) url{
    if(self.mkvUrl==nil||![self.mkvUrl isEqualToString:url]||true){
        self.mkvUrl = url;
        NSString *outpath = AppDele.localMkvM3u8PathPrefix;
        NSString* inpath =url;
        if(AppDele.simulate){
            inpath=@"/Users/zhangwei/Desktop/2.mkv";
        }
        //inpath = @"http://10.0.1.8:9090/appletv/1.mkv";
        NSLog(@"OutPath:%@",outpath);
        NSLog(@"InPath:%@",inpath);
       
        self.m3u8Url = [NSString stringWithFormat:@"%@mkv.m3u8",AppDele.localMkvM3u8UrlPrefix];
        dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^{
            transfer_code_interrupt = 1;
            @synchronized(AppDele.mkvProcess) {
                 NSDate* start = [NSDate date];
                @try {
                    transfer_code_interrupt = 0;
                    if([[NSFileManager defaultManager] fileExistsAtPath:outpath]){
                        [[NSFileManager defaultManager] removeItemAtPath:outpath error:nil];
                    }
                    [[NSFileManager defaultManager] createDirectoryAtPath:outpath withIntermediateDirectories:YES attributes:nil error:nil];
                    NSString* cookie = @"Cookie:gdriveid=08D39F59B366F371195050D992B72FD2;\r\n";
                    NSLog(@"m3u8Path:%@",[outpath stringByAppendingString:@"mkv.m3u8"]);
                    convert_avi_to_m3u8([inpath cStringUsingEncoding:NSASCIIStringEncoding],[[outpath stringByAppendingString:@"mkv.m3u8"] cStringUsingEncoding:NSASCIIStringEncoding],
                                        [[outpath stringByAppendingString:@"%04d.ts"] cStringUsingEncoding:NSASCIIStringEncoding],[cookie cStringUsingEncoding:NSASCIIStringEncoding]);
                   
                }
                @catch (NSException *exception) {
                    NSLog(@"error occured for mkv to m3u8 %@",exception);
                }
                @finally {
                    NSLog(@"Start Date:%@",[start description]);
                    NSLog(@"End Date:%@",[[NSDate date] description]);
                }
            }
            

        });
    }
}

@end
