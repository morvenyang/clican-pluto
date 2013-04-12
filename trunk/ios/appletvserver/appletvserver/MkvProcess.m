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
#import "AtvUtil.h"

@implementation MkvProcess

@synthesize mkvUrl = _mkvUrl;
@synthesize mkvM3u8Content = _mkvM3u8Content;
@synthesize m3u8Path = _m3u8Path;
-(void) dealloc{
    TT_RELEASE_SAFELY(_mkvUrl);
    TT_RELEASE_SAFELY(_m3u8Path);
    TT_RELEASE_SAFELY(_mkvM3u8Content);
    [super dealloc];
}

-(NSString*) convertToM3u8MkvUrl:(NSString*) url{
    if(self.mkvUrl==nil||![self.mkvUrl isEqualToString:url]){
        self.mkvUrl = url;
        NSString* inpath =url;
        if([self.mkvUrl rangeOfString:@"smb://"].location!=NSNotFound){
            inpath = [NSString stringWithFormat:@"http://%@:8081/appletv/noctl/proxy/smb.mkv?url=%@",AppDele.ipAddress,[AtvUtil encodeURL:url]];
        }
        NSString *outpath = AppDele.localMkvM3u8PathPrefix;
        
        if(AppDele.simulate){
            inpath=@"/Users/zhangwei/Desktop/2.mkv";
        }
        NSString* outputFile = [outpath stringByAppendingFormat:@"mkv.m3u8"];
        self.m3u8Path = outputFile;
        NSLog(@"OutPath:%@",outpath);
        NSLog(@"InPath:%@",inpath);
        dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^{
            transfer_code_interrupt = 1;
            @synchronized(AppDele.mkvProcess) {
                if([[NSFileManager defaultManager] fileExistsAtPath:outpath]){
                    [[NSFileManager defaultManager] removeItemAtPath:outpath error:nil];
                }
                [[NSFileManager defaultManager] createDirectoryAtPath:outpath withIntermediateDirectories:YES attributes:nil error:nil];
                NSDate* start = [NSDate date];
                @try {
                    transfer_code_interrupt = 0;
                    NSString* cookie = nil;
                    if([inpath rangeOfString:@"http://"].location!=NSNotFound){
                        NSArray* cookies =[[NSHTTPCookieStorage sharedHTTPCookieStorage] cookies];
                        NSString* gdriveid = nil;
                        for(int i=0;i<[cookies count];i++){
                            NSHTTPCookie* cookie = [cookies objectAtIndex:i];
                            NSLog(@"%@ %@ %@",cookie.name,cookie.value,cookie.domain);
                            if([cookie.name isEqualToString:@"gdriveid"]){
                                gdriveid = cookie.value;
                                break;
                            }
                        }
                        NSLog(@"gdriveid=%@",gdriveid);
                        cookie = [NSString stringWithFormat:@"Cookie:gdriveid=%@;\r\n",gdriveid];
                    }
                    
                    NSLog(@"m3u8Path:%@",outputFile);
                    
                    convert_avi_to_m3u8([inpath cStringUsingEncoding:NSASCIIStringEncoding],[outputFile cStringUsingEncoding:NSASCIIStringEncoding],
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
    return self.m3u8Path;
}

@end
