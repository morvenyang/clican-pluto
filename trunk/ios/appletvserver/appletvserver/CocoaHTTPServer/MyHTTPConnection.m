
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
#import "M3u8Download.h"
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
      
        NSString *respString = [req responseString];
        NSString* localM3U8String = respString;
        NSArray* lines = [respString componentsSeparatedByCharactersInSet: [NSCharacterSet newlineCharacterSet]];
        NSMutableArray* m3u8DownloadLines = [NSMutableArray array];
        M3u8Download* m3u8Download = [[M3u8Download alloc] init];
        m3u8Download.m3u8DownloadLines = m3u8DownloadLines;
        int j =0;
        for(int i=0;i<[lines count];i++){
            NSString* line = [lines objectAtIndex:i];
            if(line!=nil&&[line rangeOfString:@"http"].location==0){
                M3u8DownloadLine* m3u8DownloadLine = [[M3u8DownloadLine alloc] init];
                m3u8DownloadLine.originalUrl = line;
                m3u8DownloadLine.localUrl = [[AppDele localM3u8UrlPrefix] stringByAppendingFormat:@"%i.ts",j];
                m3u8DownloadLine.localPath = [[AppDele localM3u8PathPrefix] stringByAppendingFormat:@"%i.ts",j];
                [m3u8DownloadLines addObject:m3u8DownloadLine];
                localM3U8String = [localM3U8String stringByReplacingOccurrencesOfString:m3u8DownloadLine.originalUrl withString:m3u8DownloadLine.localUrl];
                j++;
            }
        }
        [[AppDele queue] cancelAllOperations];
        [[AppDele queue] waitUntilAllOperationsAreFinished];
        for(int i=0;i<5&&i<[m3u8DownloadLines count];i++){
            [[AppDele m3u8Process] addAsyncM3u8TSRequestByM3u8Download:m3u8Download];
        }
        return localM3U8String;
    } else {
        NSLog(@"Download m3u8 failure for url:%@, error:%@",url,error);
        return nil;
    }
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
        [[NSFileManager defaultManager] removeItemAtPath:[AppDele localM3u8PathPrefix] error:nil];
        [[NSFileManager defaultManager] createDirectoryAtPath:[AppDele localM3u8PathPrefix] withIntermediateDirectories:YES attributes:nil error:nil];
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
        
    }else if([path rangeOfString:@"/appletv/temp/m3u8"].location!=NSNotFound){
        NSString* localPath = [path stringByReplacingOccurrencesOfString:@"/appletv/temp/m3u8" withString:[AppDele localM3u8PathPrefix]];
        NSLog(@"get m3u8 from localPath:%@",localPath);
        while(true){
            if([[NSFileManager defaultManager] fileExistsAtPath:localPath]){
                HTTPFileResponse* resp = [[HTTPFileResponse alloc] initWithFilePath:localPath forConnection:self];
                return resp;
            } else {
                NSLog(@"The ts file has not been downloaded, wait for 1 second, ts:%@",localPath);
                [NSThread sleepForTimeInterval:1.0f];
            }
        }
       
    }else{
        return [super httpResponseForMethod:method URI:path];
    }
	
}

@end
