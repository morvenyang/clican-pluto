//
//  M3u8Process.m
//  appletvserver
//
//  Created by zhang wei on 13-2-27.
//
//

#import "M3u8Process.h"
#import "MyHTTPConnection.h"
#import "HTTPMessage.h"
#import "HTTPDataResponse.h"
#import "DDNumber.h"
#import "HTTPLogging.h"
#import "ASIHTTPRequest.h"
#import "M3U8TSHTTPRequest.h"
#import "AppDelegate.h"
#import "AtvUtil.h"
#import "M3u8DownloadLine.h"
#import "HTTPDataHeaderResponse.h"
#import "HTTPFileResponse.h"

@implementation M3u8Process

@synthesize m3u8Url = _m3u8Url;
@synthesize m3u8Download = _m3u8Download;
@synthesize m3u8String = _m3u8String;

-(NSString*) doSyncRequestByM3U8Url:(NSString*) url{
    NSString* currentM3u8String = NULL;
    M3u8DownloadLine* maxFinishedDownloadLine = NULL;
    if(self.m3u8Url==nil||![self.m3u8Url isEqualToString:url]){
        self.m3u8Url = url;
        [[NSFileManager defaultManager] removeItemAtPath:[AppDele localM3u8PathPrefix] error:nil];
        [[NSFileManager defaultManager] createDirectoryAtPath:[AppDele localM3u8PathPrefix] withIntermediateDirectories:YES attributes:nil error:nil];
        ASIHTTPRequest *req = [ASIHTTPRequest requestWithURL:[NSURL URLWithString:url]];
        [req startSynchronous];
        NSError *error = [req error];
        if (!error) {
            
            NSString *respString = [req responseString];
            self.m3u8String = respString;
            NSArray* lines = [respString componentsSeparatedByCharactersInSet: [NSCharacterSet newlineCharacterSet]];
            NSMutableArray* m3u8DownloadLines = [NSMutableArray array];
            self.m3u8Download = [[M3u8Download alloc] init];
            self.m3u8Download.m3u8DownloadLines = m3u8DownloadLines;
            int j =0;
            for(int i=0;i<[lines count];i++){
                NSString* line = [lines objectAtIndex:i];
                if(line!=nil&&[line rangeOfString:@"http"].location==0){
                    M3u8DownloadLine* m3u8DownloadLine = [[M3u8DownloadLine alloc] init];
                    m3u8DownloadLine.originalUrl = line;
                    m3u8DownloadLine.localUrl = [[AppDele localM3u8UrlPrefix] stringByAppendingFormat:@"%i.ts",j];
                    m3u8DownloadLine.localPath = [[AppDele localM3u8PathPrefix] stringByAppendingFormat:@"%i.ts",j];
                    [m3u8DownloadLines addObject:m3u8DownloadLine];
                    self.m3u8String = [self.m3u8String stringByReplacingOccurrencesOfString:m3u8DownloadLine.originalUrl withString:m3u8DownloadLine.localUrl];
                    j++;
                }
            }
            //        [[AppDele queue] cancelAllOperations];
            //        [[AppDele queue] waitUntilAllOperationsAreFinished];
            //        [[AppDele queue] go];
            for(int i=0;i<5&&i<[m3u8DownloadLines count];i++){
                [self addAsyncM3u8TSRequest];
            }
            while(true) {
                maxFinishedDownloadLine = [self.m3u8Download getMaxFinishedDownloadLine];
                if(maxFinishedDownloadLine!=NULL){
                    break;
                }else{
                    [NSThread sleepForTimeInterval:1.0f];
                }
            }
            NSRange range = [self.m3u8String rangeOfString:[maxFinishedDownloadLine localUrl]];
            NSRange subRange = NSMakeRange(0,range.location+range.length);
            currentM3u8String = [self.m3u8String substringWithRange:subRange];
            return currentM3u8String;
        } else {
            NSLog(@"Download m3u8 failure for url:%@, error:%@",url,error);
            return nil;
        }
    }else{
        
        while(true) {
            maxFinishedDownloadLine = [self.m3u8Download getMaxFinishedDownloadLine];
            if(maxFinishedDownloadLine!=NULL){
                break;
            }else{
                [NSThread sleepForTimeInterval:1.0f];
            }
        }
        
        NSRange range = [self.m3u8String rangeOfString:[maxFinishedDownloadLine localUrl]];
        NSRange subRange = NSMakeRange(0,range.location+range.length);
        currentM3u8String = [self.m3u8String substringWithRange:subRange];
        return currentM3u8String;
    }
}

-(void) addAsyncM3u8TSRequest{
    
    M3u8DownloadLine* m3u8DownloadLine = [self.m3u8Download getNextDownloadLine];
    if(m3u8DownloadLine==nil){
        return;
    }
    NSLog(@"downloading m3u8 ts %@",[m3u8DownloadLine.localPath stringByReplacingOccurrencesOfString:[AppDele localM3u8PathPrefix] withString:@""]);
    M3u8TSHTTPRequest *req = [M3u8TSHTTPRequest requestWithURL:[NSURL URLWithString:m3u8DownloadLine.originalUrl]];
    req.m3u8Download = self.m3u8Download;
    req.m3u8DownloadLine = m3u8DownloadLine;
    NSString *downloadPath = m3u8DownloadLine.localPath;
    NSString *tempDownloadPath = [m3u8DownloadLine.localPath stringByAppendingString:@".download"];
    // The full file will be moved here if and when the request completes successfully
    [req setDownloadDestinationPath:downloadPath];
    
    // This file has part of the download in it already
    [req setTemporaryFileDownloadPath:tempDownloadPath];
    [req setAllowResumeForFileDownloads:YES];
    [req setTimeOutSeconds:180];
    [req setDelegate:self];
    [req setDidFinishSelector:@selector(m3u8tsRequestDone:)];
    [req setDidFailSelector:@selector(m3u8tsRequestWentWrong:)];
    [[AppDele queue] addOperation:req]; //queue is an NSOperationQueue
}

- (void)m3u8tsRequestDone:(M3u8TSHTTPRequest *)req
{
    if([req isKindOfClass:[M3u8TSHTTPRequest class]]){
        M3u8TSHTTPRequest* m3u8TSReq = (M3u8TSHTTPRequest*)req;
        NSLog(@"Download finished for ts : %@",[[m3u8TSReq downloadDestinationPath] stringByReplacingOccurrencesOfString:[AppDele localM3u8PathPrefix] withString:@""]);
        M3u8Download* m3u8Download = m3u8TSReq.m3u8Download;
        req.m3u8DownloadLine.finished = YES;
        [self addAsyncM3u8TSRequest];
    }else{
        NSLog(@"Download finished for :%@",[req url]);
    }
}



- (void)m3u8tsRequestWentWrong:(M3u8TSHTTPRequest *)req
{
    NSLog(@"Download failure for ts : %@ with error: %@",[[req downloadDestinationPath] stringByReplacingOccurrencesOfString:[AppDele localM3u8PathPrefix] withString:@""], [req error]);
    //retry
    M3u8TSHTTPRequest *newreq = [M3u8TSHTTPRequest requestWithURL:[req originalURL]];
    newreq.m3u8Download = req.m3u8Download;
    newreq.m3u8DownloadLine = req.m3u8DownloadLine;
    // The full file will be moved here if and when the request completes successfully
    [newreq setDownloadDestinationPath:req.downloadDestinationPath];
    
    // This file has part of the download in it already
    [newreq setTemporaryFileDownloadPath:req.temporaryFileDownloadPath];
    [newreq setAllowResumeForFileDownloads:YES];
    [newreq setTimeOutSeconds:180];
    [newreq setDelegate:self];
    [newreq setDidFinishSelector:@selector(m3u8tsRequestDone:)];
    [newreq setDidFailSelector:@selector(m3u8tsRequestWentWrong:)];
    [[AppDele queue] addOperation:newreq];
}

@end
