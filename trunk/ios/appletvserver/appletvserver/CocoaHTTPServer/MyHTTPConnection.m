
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
#import "Constants.h"
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
    if ([path isEqualToString:@"/appletv/javascript/clican.js"]||[path isEqualToString:@"/appletv/local.xml"])
    {
        NSString  *replaceFilePath=[[AppDele localWebPathPrefix] stringByAppendingString:path];
        NSLog(@"filepath:%@",replaceFilePath);
        NSString* replaceContent = [NSString stringWithContentsOfFile:replaceFilePath encoding:NSUTF8StringEncoding error:nil];
        NSLog(@"content:%@",replaceContent);
        NSString* ipAddress = [AtvUtil getIPAddress];
        ipAddress = [ipAddress stringByAppendingString:@":8080"];
        NSLog(@"ip address=%@",ipAddress);
        
        NSRange matchRange1 = [replaceContent rangeOfString:@"local.clican.org"];
        NSRange matchRange2 = [replaceContent rangeOfString:@"/appletv"];
       
        
        NSString* matchString = [replaceContent substringWithRange:NSMakeRange(matchRange1.location, matchRange2.location-matchRange1.location)];
        replaceContent = [replaceContent stringByReplacingOccurrencesOfString:matchString withString:ipAddress];
        
        if([path isEqualToString:@"/appletv/javascript/clican.js"]){
            NSString* userAgent = [request headerField:@"User-Agent"];
            if([userAgent rangeOfString:@"Chrome"].location!=NSNotFound){
                replaceContent = [replaceContent stringByReplacingOccurrencesOfString:@"simulate : 'atv'" withString:@"simulate : 'browser'"];
            }
        }
        replaceContent = [replaceContent stringByReplacingOccurrencesOfString:@"http://www.clican.org/appletv" withString:ATV_SERVER_IP];
        NSData *response = [replaceContent dataUsingEncoding:NSUTF8StringEncoding];
        return [[HTTPDataResponse alloc] initWithData:response];
    }else if([path rangeOfString:@"/appletv/noctl/proxy/play.m3u8"].location!=NSNotFound){
        NSString* m3u8Url = [[self parseGetParams] objectForKey:@"url"];
        NSLog(@"m3u8 url:%@",m3u8Url);
        [AppDele m3u8Process].running = YES;
        [AppDele mp4Process].running = NO;
        NSString* localM3u8String = [[AppDele m3u8Process] doSyncRequestByM3U8Url:m3u8Url start:YES];
        if(localM3u8String!=nil){
            NSData *data = [localM3u8String dataUsingEncoding:NSUTF8StringEncoding];
            HTTPDataResponse* resp=[[HTTPDataResponse alloc] initWithData:data];
            return resp;
        }else{
            return nil;
        }
    }else if([path rangeOfString:@"/appletv/noctl/proxy/temp/m3u8"].location!=NSNotFound){
        NSString* m3u8Url = [[self parseGetParams] objectForKey:@"m3u8Url"];
        NSLog(@"m3u8Url=%@",m3u8Url);
        NSRange range = [path rangeOfString:@"?"];
        path = [path substringWithRange:NSMakeRange(0, range.location)];
        NSString* localPath = [path stringByReplacingOccurrencesOfString:@"/appletv/noctl/proxy/temp/m3u8" withString:[AppDele localM3u8PathPrefix]];
        if([[AppDele m3u8Process] m3u8Url]==nil||![[[AppDele m3u8Process] m3u8Url] isEqualToString:m3u8Url]){
            NSLog(@"m3u8Url is changed, we must reprocess it");
            [[AppDele m3u8Process] doSyncRequestByM3U8Url:m3u8Url start:NO];
            [[AppDele m3u8Process] seekDownloadLine:localPath];
            [[AppDele m3u8Process] start];
        }
        NSLog(@"get m3u8 from localPath:%@",localPath);
        BOOL seek = YES;
        while(true){
            if(![m3u8Url isEqualToString:[AppDele m3u8Process].m3u8Url]){
                break;
            }
            if([[NSFileManager defaultManager] fileExistsAtPath:localPath]){
                HTTPFileResponse* resp = [[HTTPFileResponse alloc] initWithFilePath:localPath forConnection:self];
                return resp;
            } else {
                if(seek){
                    seek = NO;
                    [[AppDele m3u8Process] seekDownloadLine:localPath];
                }
                NSLog(@"The ts file has not been downloaded, wait for 1 second, ts:%@",localPath);
                [NSThread sleepForTimeInterval:1.0f];
            }
        }
    }else if([path rangeOfString:@"/appletv/noctl/proxy/play.mp4"].location!=NSNotFound){
        NSString* mp4Url = [[self parseGetParams] objectForKey:@"url"];
        NSLog(@"mp4 url:%@",mp4Url);
        [AppDele m3u8Process].running = NO;
        [AppDele mp4Process].running = YES;
        Mp4Download* mp4Download = [AppDele mp4Process].mp4Download;
       
        if(mp4Download!=nil&&[mp4Download.mp4Url isEqualToString:mp4Url]){
            NSString* range = [request headerField:@"Range"];
            if(range==nil||[range length]==0){
                range = @"bytes=0-";
            }
            
            NSLog(@"Range:%@",range);
            NSArray* crs = [[range stringByReplacingOccurrencesOfString:@"bytes=" withString:@""] componentsSeparatedByString:@"-"];
            long startPosition = [(NSString*)[crs objectAtIndex:0] longLongValue];
            long endPosition = startPosition+MP4_PARTIAL_LENGTH-1;
            if([crs objectAtIndex:1]!=nil&&[(NSString*)[crs objectAtIndex:1] length]>0){
                endPosition = [(NSString*)[crs objectAtIndex:1] longLongValue]+1;
            }
            NSLog(@"Rage:%ld-%ld",startPosition,endPosition);
            Mp4Download* mp4Download = [AppDele mp4Process].mp4Download;
            
            NSData *data = [mp4Download getDataByStartPosition:startPosition endPosition:endPosition];
            NSLog(@"data length:%i",[data length]);
            HTTPDataHeaderResponse* resp=[[HTTPDataHeaderResponse alloc] initWithData:data status:206];
            
            [[resp httpHeaders] setValue:@"video/mp4" forKey:@"Content-Type"];
            NSString *rangeStr = [NSString stringWithFormat:@"%ld-%ld", startPosition, startPosition+[data length]-1];
            NSString *contentRangeStr = [NSString stringWithFormat:@"bytes %@/%ld", rangeStr, mp4Download.totalLength];
            
            [[resp httpHeaders] setValue:contentRangeStr forKey:@"Content-Range"];
            NSLog(@"Content-Range:%@",contentRangeStr);
            [[resp httpHeaders] setValue:[NSString stringWithFormat:@"%i",[data length]] forKey:@"Content-Length"];
            
            return resp;
        } else {
            Mp4Download* mp4Download = [[AppDele mp4Process] doSyncRequestByMP4Url:mp4Url];
            NSData* data = [NSData dataWithContentsOfFile:[[mp4Download.mp4DownloadPartials objectAtIndex:0] localPath]];
                       HTTPDataHeaderResponse* resp=[[HTTPDataHeaderResponse alloc] initWithData:data status:206];
            [[resp httpHeaders] setValue:@"video/mp4" forKey:@"Content-Type"];
            [[resp httpHeaders] setValue:[NSString stringWithFormat:@"%i",[data length]] forKey:@"Content-Length"];
            NSString *rangeStr = [NSString stringWithFormat:@"%i-%i", 0, [data length]-1];
            NSString *contentRangeStr = [NSString stringWithFormat:@"bytes %@/%ld", rangeStr, mp4Download.totalLength];
            [[resp httpHeaders] setValue:contentRangeStr forKey:@"Content-Range"];
            return resp;
        }
    }else if([path isEqualToString:@"/appletv/noctl/xunlei/getsession.do"]){
        NSArray* cookies =[[NSHTTPCookieStorage sharedHTTPCookieStorage] cookies];
        NSString* sessionid = @"";
        NSString* vip = @"";
        NSString* userid = @"";
        for(int i=0;i<[cookies count];i++){
            NSHTTPCookie* cookie = [cookies objectAtIndex:i];
            NSLog(@"%@ %@ %@",cookie.name,cookie.value,cookie.domain);
            if([cookie.domain isEqualToString:@".xunlei.com"]){
                if([cookie.name isEqualToString:@"userid"]){
                    userid = cookie.value;
                }else if([cookie.name isEqualToString:@"lsessionid"]){
                    sessionid = cookie.value;
                }else if([cookie.name isEqualToString:@"isvip"]){
                    vip = cookie.value;
                }
            }
        }
        NSString* content = [NSString stringWithFormat:@"{\"sessionid\":\"%@\",\"userid\":\"%@\",\"vip\":\"%@\"}",sessionid,userid,vip];
        NSLog(@"%@",content);
        NSData *data = [content dataUsingEncoding:NSUTF8StringEncoding];
        HTTPDataResponse* resp=[[HTTPDataResponse alloc] initWithData:data];
        return resp;
    }else{
        return [super httpResponseForMethod:method URI:path];
    }
    return nil;
}

@end
