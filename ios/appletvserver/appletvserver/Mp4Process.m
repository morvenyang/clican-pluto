//
//  Mp4Process.m
//  appletvserver
//
//  Created by zhang wei on 13-2-28.
//
//

#import "Mp4Process.h"
#import "MyHTTPConnection.h"
#import "HTTPMessage.h"
#import "HTTPDataResponse.h"
#import "DDNumber.h"
#import "HTTPLogging.h"
#import "ASIHTTPRequest.h"
#import "AppDelegate.h"
#import "AtvUtil.h"
#import "Mp4DownloadPartial.h"
#import "HTTPDataHeaderResponse.h"
#import "HTTPFileResponse.h"

@implementation Mp4Process

@synthesize mp4Url = _mp4Url;
@synthesize mp4Download = _mp4Download;

-(NSData*) doSyncRequestByMP4Url:(NSString*) url{
    self.mp4Url = url;
    
    [[NSFileManager defaultManager] removeItemAtPath:[AppDele localMp4PathPrefix] error:nil];
    [[NSFileManager defaultManager] createDirectoryAtPath:[AppDele localMp4PathPrefix] withIntermediateDirectories:YES attributes:nil error:nil];
    
    ASIHTTPRequest *req = [ASIHTTPRequest requestWithURL:[NSURL URLWithString:url]];
    NSMutableDictionary* headers = [NSMutableDictionary dictionary];
    [headers setValue:@"bytes=0-1048576" forKey:@"Range"];
    
    [req setRequestHeaders:headers];
    [req startSynchronous];
    NSError *error = [req error];
    if (!error) {
        NSData* data = [req responseData];
        NSMutableArray* mp4DownloadPartials = [NSMutableArray array];
        self.mp4Download = [[Mp4Download alloc] init];
        self.mp4Download.mp4DownloadPartials = mp4DownloadPartials;
        NSString* contentRange = [[req responseHeaders] valueForKey:@"Content-Range"];
        NSArray* crs = [contentRange componentsSeparatedByCharactersInSet: [NSCharacterSet symbolCharacterSet]];
        if([crs count]==3){
            long startPosition = [(NSString*)[crs objectAtIndex:0] longLongValue];
            long endPosition = [(NSString*)[crs objectAtIndex:1] longLongValue];
            long totalLength = [(NSString*)[crs objectAtIndex:2] longLongValue];
            long partialSize = (totalLength-endPosition)/(1024*1024)+1;
            Mp4DownloadPartial* firstPartial = [[[Mp4DownloadPartial alloc] init] autorelease];
            firstPartial.startPosition = startPosition;
            firstPartial.endPosition = endPosition;
            firstPartial.localPath = [[AppDele localMp4PathPrefix] stringByAppendingString:@"0.mp4.download"];
            [mp4DownloadPartials addObject:firstPartial];
            for(int i=0;i<partialSize;i++){
                Mp4DownloadPartial* partial = [[[Mp4DownloadPartial alloc] init] autorelease];
                partial.startPosition = endPosition+(i)*(1024*1024);
                partial.endPosition = partial.startPosition+(1024*1024);
                
                partial.localPath = [[AppDele localMp4PathPrefix] stringByAppendingFormat:@"%i.mp4.download",i+1];
                if(partial.startPosition>totalLength){
                    break;
                }
                [mp4DownloadPartials addObject:partial];
                if(partial.endPosition>totalLength){
                    partial.endPosition = totalLength;
                    break;
                }
            }
        } else {
            NSLog(@"The server side doesn't support HTTP Range");
            return nil;
        }
    }
    return nil;
}


@end
