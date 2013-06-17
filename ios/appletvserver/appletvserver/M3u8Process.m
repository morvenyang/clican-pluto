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
@synthesize m3u8RelativeUrl = _m3u8RelativeUrl;
@synthesize m3u8Download = _m3u8Download;
@synthesize m3u8String = _m3u8String;
@synthesize running = _running;


-(void) dealloc{
    TT_RELEASE_SAFELY(_m3u8Url);
    TT_RELEASE_SAFELY(_m3u8RelativeUrl);
    TT_RELEASE_SAFELY(_m3u8Download);
    TT_RELEASE_SAFELY(_m3u8String);
    [super dealloc];
}
-(NSString*) doSyncRequestByM3U8Url:(NSString*) url simulate:(NSString *)simulate start:(BOOL) start{
    if(self.m3u8Url==nil||![self.m3u8Url isEqualToString:url]){
        self.m3u8Url = url;
        NSRange lastSlahRange = [url rangeOfString:@"/" options:NSBackwardsSearch];
        if(lastSlahRange.location!=NSNotFound){
            NSRange relativeRange = NSMakeRange(0,lastSlahRange.location);
            self.m3u8RelativeUrl = [url substringWithRange:relativeRange];
        }else{
            self.m3u8RelativeUrl = url;
        }
        
        [[NSFileManager defaultManager] removeItemAtPath:[AppDele localM3u8PathPrefix] error:nil];
        [[NSFileManager defaultManager] createDirectoryAtPath:[AppDele localM3u8PathPrefix] withIntermediateDirectories:YES attributes:nil error:nil];
        ASIHTTPRequest *req = [ASIHTTPRequest requestWithURL:[NSURL URLWithString:url]];
        [req setShouldContinueWhenAppEntersBackground:YES];
        [req startSynchronous];
        NSError *error = [req error];
        if (!error) {
            
            NSString *respString = [req responseString];
            self.m3u8String = respString;
            NSArray* lines = [respString componentsSeparatedByCharactersInSet: [NSCharacterSet newlineCharacterSet]];
            NSMutableArray* m3u8DownloadLines = [NSMutableArray array];
            self.m3u8Download = [[M3u8Download alloc] init];
            self.m3u8Download.m3u8Url = url;
            self.m3u8Download.m3u8DownloadLines = m3u8DownloadLines;
            int j =0;
            for(int i=0;i<[lines count];i++){
                NSString* line = [lines objectAtIndex:i];
                if(line!=nil&&[line length]!=0&&[line rangeOfString:@"#"].location!=0){
                    //NSLog(@"line:%@",line);
                    M3u8DownloadLine* m3u8DownloadLine = [[[M3u8DownloadLine alloc] init] autorelease];
                    m3u8DownloadLine.originalUrl = line;
                    if([simulate isEqualToString:@"native"]){
                         m3u8DownloadLine.localUrl = [[AppDele localNativeM3u8UrlPrefix] stringByAppendingFormat:@"%i.ts",j];
                    }else{
                        m3u8DownloadLine.localUrl = [[AppDele localM3u8UrlPrefix] stringByAppendingFormat:@"%i.ts",j];
                    }
                    
                    m3u8DownloadLine.localPath = [[AppDele localM3u8PathPrefix] stringByAppendingFormat:@"%i.ts",j];
                    [m3u8DownloadLines addObject:m3u8DownloadLine];
                    self.m3u8String = [self.m3u8String stringByReplacingOccurrencesOfString:m3u8DownloadLine.originalUrl withString:[m3u8DownloadLine.localUrl stringByAppendingFormat:@"?m3u8Url=%@",[AtvUtil encodeURL:url]]];
                    j++;
                }
            }
            [[AppDele queue] cancelAllOperations];
            [[AppDele queue] waitUntilAllOperationsAreFinished];
            [[AppDele queue] go];
            if(start){
                 [self start];
            }
            return self.m3u8String;
        } else {
            NSLog(@"Download m3u8 failure for url:%@, error:%@",url,error);
            return nil;
        }
    }else{
        return self.m3u8String;
    }
}

-(void) seekDownloadLine:(NSString*) localPath{
    [[self m3u8Download] seekDownloadLine:localPath];
}

-(void)start{
    for(int i=0;i<5&&i<[self.m3u8Download.m3u8DownloadLines count];i++){
        [self addAsyncM3u8TSRequest];
    }
}

-(void) addAsyncM3u8TSRequest{
    if(![AtvUtil isWifi]&&!AppDele.ttgNetwork){
        NSLog(@"2G/3G网络下无法代理下载");
        return;
    }
    M3u8DownloadLine* m3u8DownloadLine = [self.m3u8Download getNextDownloadLine];
    if(m3u8DownloadLine==nil){
        return;
    }
    NSLog(@"downloading m3u8 ts %@",[m3u8DownloadLine.localPath stringByReplacingOccurrencesOfString:[AppDele localM3u8PathPrefix] withString:@""]);
    M3u8TSHTTPRequest *req = NULL;
    if([m3u8DownloadLine.originalUrl rangeOfString:@"http"].location==0){
       req = [M3u8TSHTTPRequest requestWithURL:[NSURL URLWithString:m3u8DownloadLine.originalUrl]];
    }else{
        req = [M3u8TSHTTPRequest requestWithURL:[NSURL URLWithString:[self.m3u8RelativeUrl stringByAppendingFormat:@"/%@",m3u8DownloadLine.originalUrl]]];
    }
    [req setShouldContinueWhenAppEntersBackground:YES];
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
        req.m3u8DownloadLine.finished = YES;
        if([req.m3u8Download.m3u8Url isEqualToString:self.m3u8Url]&&_running){
            [self addAsyncM3u8TSRequest];
        }else{
            self.m3u8Url = nil;
            NSLog(@"Task cancelled by another m3u8");
        }
    }else{
        NSLog(@"Download finished for :%@",[req url]);
    }
}



- (void)m3u8tsRequestWentWrong:(M3u8TSHTTPRequest *)req
{
    NSLog(@"Download failure for ts : %@ with error: %@",[[req downloadDestinationPath] stringByReplacingOccurrencesOfString:[AppDele localM3u8PathPrefix] withString:@""], [req error]);
    if(![req.m3u8Download.m3u8Url isEqualToString:self.m3u8Url]||!_running){
        NSLog(@"Task cancelled by another m3u8");
        self.m3u8Url = nil;
        return;
    }
    //retry
    M3u8TSHTTPRequest *newreq = [M3u8TSHTTPRequest requestWithURL:[req originalURL]];
    [newreq setShouldContinueWhenAppEntersBackground:YES];
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
