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
#import "Constants.h"
#import "Mp4HTTPRequest.h"

@implementation Mp4Process

@synthesize mp4Url = _mp4Url;
@synthesize mp4Download = _mp4Download;
@synthesize running = _running;

-(Mp4Download*) doSyncRequestByMP4Url:(NSString*) url{
    self.mp4Url = url;
    
    [[NSFileManager defaultManager] removeItemAtPath:[AppDele localMp4PathPrefix] error:nil];
    [[NSFileManager defaultManager] createDirectoryAtPath:[AppDele localMp4PathPrefix] withIntermediateDirectories:YES attributes:nil error:nil];
    
    ASIHTTPRequest *req = [ASIHTTPRequest requestWithURL:[NSURL URLWithString:url]];
    NSMutableDictionary* headers = [NSMutableDictionary dictionary];
    [headers setValue:[@"bytes=0-" stringByAppendingFormat:@"%i",MP4_PARTIAL_LENGTH-1] forKey:@"Range"];
    [req setRequestHeaders:headers];
    [req startSynchronous];
    NSError *error = [req error];
    if (!error) {
        NSData* data = [req responseData];
        NSMutableArray* mp4DownloadPartials = [NSMutableArray array];
        self.mp4Download = [[Mp4Download alloc] init];
        self.mp4Download.mp4DownloadPartials = mp4DownloadPartials;
        self.mp4Download.mp4Url = url;
        NSString* contentRange = [[req responseHeaders] valueForKey:@"Content-Range"];
        if(contentRange!=nil&&[contentRange rangeOfString:@"bytes"].location!=NSNotFound){
            NSMutableCharacterSet* set = [NSMutableCharacterSet new];
            [set addCharactersInString:@"-"];
            [set addCharactersInString:@"/"];
            NSArray* crs = [[contentRange stringByReplacingOccurrencesOfString:@"bytes " withString:@""] componentsSeparatedByCharactersInSet: set];
            long startPosition = [(NSString*)[crs objectAtIndex:0] longLongValue];
            long endPosition = [(NSString*)[crs objectAtIndex:1] longLongValue]+1;
            long totalLength = [(NSString*)[crs objectAtIndex:2] longLongValue];
            self.mp4Download.totalLength = totalLength;
            long partialSize = (totalLength-endPosition)/MP4_PARTIAL_LENGTH+1;
            Mp4DownloadPartial* firstPartial = [[[Mp4DownloadPartial alloc] init] autorelease];
            firstPartial.startPosition = startPosition;
            firstPartial.endPosition = endPosition;
            
            firstPartial.localPath = [[AppDele localMp4PathPrefix] stringByAppendingString:@"0.mp4"];
            [data writeToFile:firstPartial.localPath atomically:YES];
            firstPartial.finished= YES;
            [mp4DownloadPartials addObject:firstPartial];
            for(int i=0;i<partialSize;i++){
                Mp4DownloadPartial* partial = [[[Mp4DownloadPartial alloc] init] autorelease];
                partial.startPosition = endPosition+(i)*MP4_PARTIAL_LENGTH;
                partial.endPosition = partial.startPosition+MP4_PARTIAL_LENGTH;
                partial.finished = NO;
                partial.localPath = [[AppDele localMp4PathPrefix] stringByAppendingFormat:@"%i.mp4",i+1];
                if(partial.startPosition>totalLength){
                    break;
                }
                [mp4DownloadPartials addObject:partial];
                if(partial.endPosition>totalLength){
                    partial.endPosition = totalLength;
                    break;
                }
            }
            for(int i=0;i<5&&i<[mp4DownloadPartials count];i++){
                [self addAsyncMp4Request];
            }
        } else {
            NSLog(@"The server side doesn't support HTTP Range");
            return nil;
        }
    }
    return self.mp4Download;
}

-(void) addAsyncMp4Request{
    if(![AtvUtil isWifi]&&!AppDele.ttgNetwork){
        NSLog(@"2G/3G网络下无法代理下载");
        return;
    }
    Mp4DownloadPartial* mp4DownloadPartial = [self.mp4Download getNextDownloadPartial];
    if(mp4DownloadPartial==nil){
        return;
    }
    NSLog(@"downloading mp4 %@",[mp4DownloadPartial.localPath stringByReplacingOccurrencesOfString:[AppDele localMp4PathPrefix] withString:@""]);
    Mp4HTTPRequest *req = NULL;
    req = [Mp4HTTPRequest requestWithURL:[NSURL URLWithString:self.mp4Url]];
    NSMutableDictionary* headers = [NSMutableDictionary dictionary];
    [headers setValue:[@"bytes=" stringByAppendingFormat:@"%ld-%ld",mp4DownloadPartial.startPosition,mp4DownloadPartial.endPosition-1] forKey:@"Range"];
    [req setRequestHeaders:headers];
    req.mp4Download = self.mp4Download;
    req.mp4DownloadPartial = mp4DownloadPartial;
    NSString *downloadPath = mp4DownloadPartial.localPath;
    NSString *tempDownloadPath = [mp4DownloadPartial.localPath stringByAppendingString:@".download"];
    // The full file will be moved here if and when the request completes successfully
    [req setDownloadDestinationPath:downloadPath];
    
    // This file has part of the download in it already
    [req setTemporaryFileDownloadPath:tempDownloadPath];
    [req setAllowResumeForFileDownloads:YES];
    [req setTimeOutSeconds:180];
    [req setDelegate:self];
    [[AppDele queue] addOperation:req]; //queue is an NSOperationQueue
}


- (void)mp4RequestDone:(Mp4HTTPRequest *)req
{
    NSLog(@"Download finished for ts : %@",[[req downloadDestinationPath] stringByReplacingOccurrencesOfString:[AppDele localMp4PathPrefix] withString:@""]);
    if(![req.mp4Download.mp4Url isEqualToString:self.mp4Url]||!_running){
        self.mp4Url = nil;
        NSLog(@"Task cancelled by another mp4");
        return;
    }
    
    NSString* contentRange = [[req responseHeaders] valueForKey:@"Content-Range"];
    
    if(contentRange!=nil&&[contentRange rangeOfString:@"bytes"].location!=NSNotFound){
        NSMutableCharacterSet* set = [NSMutableCharacterSet new];
        [set addCharactersInString:@"-"];
        [set addCharactersInString:@"/"];
        NSArray* crs = [[contentRange stringByReplacingOccurrencesOfString:@"bytes " withString:@""] componentsSeparatedByCharactersInSet: set];
        long startPosition = [(NSString*)[crs objectAtIndex:0] longLongValue];
        long endPosition = [(NSString*)[crs objectAtIndex:1] longLongValue];
        if(req.mp4DownloadPartial.startPosition==startPosition){
            if(req.mp4DownloadPartial.endPosition-1==endPosition){
                req.mp4DownloadPartial.finished = YES;
                [self addAsyncMp4Request];
            }else{
                NSLog(@"partial download not finished");
            }
        }else{
            NSLog(@"error occured");
        }
    }else{
        NSLog(@"error occured doesn't support Content Range");
    }
    
}

- (void)mp4RequestWentWrong:(Mp4HTTPRequest *)req
{
    NSLog(@"Download failure for ts : %@ with error: %@",[[req downloadDestinationPath] stringByReplacingOccurrencesOfString:[AppDele localM3u8PathPrefix] withString:@""], [req error]);
    if(![req.mp4Download.mp4Url isEqualToString:self.mp4Url]||!_running){
        self.mp4Url = nil;
        NSLog(@"Task cancelled by another m3u8");
        return;
    }
    //retry
    Mp4HTTPRequest *newreq = [Mp4HTTPRequest requestWithURL:[req originalURL]];
    NSMutableDictionary* headers = [NSMutableDictionary dictionary];
    [headers setValue:[@"bytes=" stringByAppendingFormat:@"%ld-%ld",req.mp4DownloadPartial.startPosition,req.mp4DownloadPartial.endPosition] forKey:@"Range"];
    [newreq setRequestHeaders:headers];
    newreq.mp4Download = req.mp4Download;
    
    newreq.mp4DownloadPartial = req.mp4DownloadPartial;
    // The full file will be moved here if and when the request completes successfully
    [newreq setDownloadDestinationPath:req.downloadDestinationPath];
    
    // This file has part of the download in it already
    [newreq setTemporaryFileDownloadPath:req.temporaryFileDownloadPath];
    [newreq setAllowResumeForFileDownloads:YES];
    [newreq setTimeOutSeconds:180];
    [newreq setDelegate:self];
    [newreq setDidFinishSelector:@selector(mp4RequestDone:)];
    [newreq setDidFailSelector:@selector(mp4RequestWentWrong:)];
    [[AppDele queue] addOperation:newreq];
}

@end
