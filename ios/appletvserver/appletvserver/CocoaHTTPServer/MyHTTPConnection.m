
#import "MyHTTPConnection.h"
#import "HTTPMessage.h"
#import "HTTPDataResponse.h"
#import "DDNumber.h"
#import "HTTPLogging.h"
#import "ASIHTTPRequest.h"
#import "AppDelegate.h"
#import "AtvUtil.h"
#import "M3u8DownloadLine.h"
#import "HTTPDataHeaderResponse.h"
// Log levels : off, error, warn, info, verbose
// Other flags: trace
static const int httpLogLevel = HTTP_LOG_LEVEL_WARN; // | HTTP_LOG_FLAG_TRACE;


/**
 * All we have to do is override appropriate methods in HTTPConnection.
**/

@implementation MyHTTPConnection



-(NSString*) doSyncRequestByM3U8Url:(NSString*) url{
    ASIHTTPRequest *req = [ASIHTTPRequest requestWithURL:[NSURL URLWithString:url]];
    [req startSynchronous];
    NSError *error = [req error];
    if (!error) {
        NSString* localPrefix = [@"http://" stringByAppendingFormat:@"%@:8080/appletv/temp/m3u8/",[AtvUtil getIPAddress]];
        NSString *respString = [req responseString];
        NSString* localM3U8String = respString;
        NSArray* lines = [respString componentsSeparatedByCharactersInSet: [NSCharacterSet newlineCharacterSet]];
        NSMutableArray* m3u8DownloadLines = [NSMutableArray array];
        int j=0;
        for(int i=0;i<[lines count];i++){
            M3u8DownloadLine* m3u8DownloadLine = [[M3u8DownloadLine alloc] init];
            NSString* line = [lines objectAtIndex:i];
            if([line rangeOfString:@"#"].location!=0){
                m3u8DownloadLine.originalUrl = line;
                m3u8DownloadLine.localUrl = [localPrefix stringByAppendingFormat:@"%i.ts",j];
                j++;
                [m3u8DownloadLines addObject:line];
                localM3U8String = [localM3U8String stringByReplacingOccurrencesOfString:m3u8DownloadLine.originalUrl withString:m3u8DownloadLine.localUrl];
            }
        }

        return localM3U8String;
    }else{
        NSLog(@"Download m3u8 failure for url:%@, error:%@",url,error);
        return nil;
    }
}


-(void) addAsyncRequestByUrl:(NSString*) url{
    ASIHTTPRequest *req = [ASIHTTPRequest requestWithURL:[NSURL URLWithString:url]];
    NSString *downloadPath = @"/Users/zhangwei/Desktop/m3u8/my_work_in_progress.txt";
    
    // The full file will be moved here if and when the request completes successfully
    [req setDownloadDestinationPath:downloadPath];
    
    // This file has part of the download in it already
    [req setTemporaryFileDownloadPath:@"/Users/zhangwei/Desktop/m3u8/my_work_in_progress.txt.download"];
    [req setAllowResumeForFileDownloads:YES];
    
    [req setDelegate:self];
    [req setDidFinishSelector:@selector(requestDone:)];
    [req setDidFailSelector:@selector(requestWentWrong:)];
    [[AppDele queue] addOperation:req]; //queue is an NSOperationQueue
}

- (void)requestDone:(ASIHTTPRequest *)req
{
    NSLog(@"Download finished for :%@",[req url]);
}

- (void)m3u8RequestDone:(ASIHTTPRequest *)req
{
    NSLog(@"Download m3u8 finished for :%@",[req url]);
    
}

- (void)requestWentWrong:(ASIHTTPRequest *)req
{
    NSLog(@"Download failure for :%@, error:%@",[req url],[req error]);
}



- (NSObject<HTTPResponse> *)httpResponseForMethod:(NSString *)method URI:(NSString *)path
{
	HTTPLogTrace();
	
	if ([path isEqualToString:@"/appletv/javascript/clican.js"]||[path isEqualToString:@"/appletv/releasenote.xml"])
	{
		NSString  *replaceFilePath=[[[NSBundle mainBundle] bundlePath] stringByAppendingPathComponent:[@"web" stringByAppendingString:path]];
        NSString* replaceContent = [NSString stringWithContentsOfFile:replaceFilePath encoding:NSUTF8StringEncoding error:nil];
        NSString* ipAddress = [AtvUtil getIPAddress];
        ipAddress = [ipAddress stringByAppendingString:@":8080"];
        NSLog(@"ip address=%@",ipAddress);
        replaceContent = [replaceContent stringByReplacingOccurrencesOfString:@"clican.org" withString:ipAddress];
		NSData *response = [replaceContent dataUsingEncoding:NSUTF8StringEncoding];
		return [[HTTPDataResponse alloc] initWithData:response];
	}else if([path rangeOfString:@"/appletv/proxy/m3u8"].location!=NSNotFound){
        NSString* m3u8Url = [[self parseGetParams] objectForKey:@"url"];
        NSLog(@"m3u8 url:%@",m3u8Url);
        NSString* localM3u8String = [self doSyncRequestByM3U8Url:m3u8Url];
        if(localM3u8String!=nil){
            NSData *response = [localM3u8String dataUsingEncoding:NSUTF8StringEncoding];
            HTTPDataHeaderResponse* resp=[[HTTPDataHeaderResponse alloc] initWithData:response];
            [[resp httpHeaders] setValue:@"application/octet-stream" forKey:@"Content-Type"];
            [[resp httpHeaders] setValue:@"attachment;filename=\"1.m3u8\"" forKey:@"Content-Disposition"];
            [[resp httpHeaders] setValue:[NSString stringWithFormat:@"%i",[response length]] forKey:@"Content-Length"];
            return resp;
        }else{
            return nil;
        }
        
    }else{
        return [super httpResponseForMethod:method URI:path];
    }
	
}

@end
