
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
#import "HTTPRedirectResponse.h"
// Log levels : off, error, warn, info, verbose
// Other flags: trace
static const int httpLogLevel = HTTP_LOG_LEVEL_WARN; // | HTTP_LOG_FLAG_TRACE;


/**
 * All we have to do is override appropriate methods in HTTPConnection.
**/

@implementation MyHTTPConnection





- (NSObject<HTTPResponse> *)httpResponseForMethod:(NSString *)method URI:(NSString *)path
{
	HTTPLogTrace();
	NSLog(@"path:%@",path);
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
        NSString* localM3u8String = [[AppDele m3u8Process] doSyncRequestByM3U8Url:m3u8Url];
        if(localM3u8String!=nil){
            NSData *data = [localM3u8String dataUsingEncoding:NSUTF8StringEncoding];
            HTTPDataHeaderResponse* resp=[[HTTPDataHeaderResponse alloc] initWithData:data];
            [[resp httpHeaders] setValue:@"audio/x-mpegurl" forKey:@"Content-Type"];
            return resp;
        }else{
            return nil;
        }
    }else if([path rangeOfString:@"/appletv/temp/m3u8"].location!=NSNotFound){
        NSString* localPath = [path stringByReplacingOccurrencesOfString:@"/appletv/temp/m3u8/" withString:[AppDele localM3u8PathPrefix]];
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
