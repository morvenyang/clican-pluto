
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
#import "MkvProcess.h"
#import "KxSMBProvider.h"
#import "HTTPSMBResponse.h"
#import "ProcessManager.h"
#import "ffmpeg.h"
#import "HTTPMediaFileResponse.h"
// Log levels : off, error, warn, info, verbose
// Other flags: trace
static const int httpLogLevel = HTTP_LOG_LEVEL_WARN; // | HTTP_LOG_FLAG_TRACE;


/**
 * All we have to do is override appropriate methods in HTTPConnection.
 **/

@implementation MyHTTPConnection





- (NSObject<HTTPResponse> *)httpResponseForMethod:(NSString *)method URI:(NSString *)path
{
    @try {
        HTTPLogTrace();
        NSLog(@"path:%@,uri:%@",path,request.url);
        
        if ([AtvUtil content:path contains:@"/appletv/javascript/clican.js"]||[AtvUtil content:path contains:@"/appletv/local.xml"])
        {
            return [self handleStaticResource:path];
        }else if([path rangeOfString:@"/appletv/noctl/proxy/play.m3u8"].location!=NSNotFound){
            return [self handleM3u8Play:path];
        }else if([path rangeOfString:@"/appletv/noctl/proxy/temp/m3u8"].location!=NSNotFound){
            return [self handleProxyTempM3u8:path];
        }else if([path rangeOfString:@"/appletv/noctl/proxy/play.mp4"].location!=NSNotFound){
            return [self handleMp4Play:path];
        }else if([path isEqualToString:@"/appletv/noctl/xunlei/getsession.do"]){
            return [self handleXunleiGetSession:path];
        }else if([path isEqualToString:@"/appletv/noctl/photo/list.json"]){
            return [self handlePhotoList:path];
        }else if([path rangeOfString:@"/appletv/noctl/photo/detail"].location!=NSNotFound){
            return [self handlePhotoDetail:path];
        }else if([path rangeOfString:@"/appletv/noctl/mkv/play.m3u8"].location!=NSNotFound){
            return [self handleMkvPlay:path];
        }else if([path rangeOfString:@"/appletv/noctl/proxy/temp/mkvM3u8"].location!=NSNotFound){
            return [self handleProxyTempMkvM3u8:path];
        }else if([path rangeOfString:@"/appletv/noctl/proxy/smb"].location!=NSNotFound){
            return [self handleProxySmb:path];
        }else if([path rangeOfString:@"/appletv/noctl/smb/resource"].location!=NSNotFound){
            return [self handleSmbResource:path];
        }else if([path rangeOfString:@"/appletv/noctl/local/resource"].location!=NSNotFound){
            return [self handleLocalResource];
        }else if([path rangeOfString:@"/appletv/noctl/proxy/play.mp3"].location!=NSNotFound){
            NSString* mp3Url = [[self parseGetParams] objectForKey:@"url"];
            if(AppDele.smbProcess.smbPath==nil||![AppDele.smbProcess.smbPath isEqualToString:mp3Url]){
                AppDele.smbProcess.smbPath = mp3Url;
                if(AppDele.smbProcess.smbFile!=nil){
                    [AppDele.smbProcess.smbFile close];
                }
                AppDele.smbProcess.smbFile = [KxSMBProvider fetchAtPath:mp3Url];
            }
            HTTPSMBResponse* resp=[[HTTPSMBResponse alloc] initWithSmbFile:AppDele.smbProcess.smbFile];
            return resp;
        }else if([path rangeOfString:@"/appletv/noctl/subtitle/download.txt"].location!=NSNotFound){
            return [self handleSubTitle:path];
        }else{
            return [super httpResponseForMethod:method URI:path];
        }
    }
    @catch (NSException *exception) {
        NSLog(@"%@",exception);
        return [super httpResponseForMethod:method URI:path];
    }
    
    
}
- (NSObject<HTTPResponse> *) handleStaticResource:(NSString*) path{
    NSString* realPath = path;
    if([path rangeOfString:@"/appletv/local.xml"].location!=NSNotFound){
        NSString* deviceId= [[self parseGetParams] objectForKey:@"deviceId"];
        if(deviceId!=nil&&deviceId.length>0){
            AppDele.atvDeviceId = deviceId;
            NSUserDefaults* defaults = [NSUserDefaults standardUserDefaults];
            [defaults setValue:deviceId forKey:ATV_DEVICE_ID_NAME];
        }
        realPath = @"/appletv/local.xml";
    }
    NSString  *replaceFilePath=[[AppDele localWebPathPrefix] stringByAppendingString:realPath];
    NSLog(@"filepath:%@",replaceFilePath);
    NSString* replaceContent = [NSString stringWithContentsOfFile:replaceFilePath encoding:NSUTF8StringEncoding error:nil];
    //NSLog(@"content:%@",replaceContent);
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
    replaceContent = [replaceContent stringByReplacingOccurrencesOfString:@"http://www.clican.org" withString:AppDele.serverIP];
    NSLog(@"%@",replaceContent);
    NSData *response = [replaceContent dataUsingEncoding:NSUTF8StringEncoding];
    return [[HTTPDataResponse alloc] initWithData:response];
}
- (NSObject<HTTPResponse> *) handleM3u8Play:(NSString*) path{
    NSString* m3u8Url = [[self parseGetParams] objectForKey:@"url"];
    NSString* simulate = @"atv";
    if([request.url.host rangeOfString:@"localhost"].location!=NSNotFound){
        simulate = @"native";
    }
    NSLog(@"m3u8 url:%@",m3u8Url);
    [ProcessManager changeRunningProcess:@"m3u8"];
    NSString* localM3u8String = [[AppDele m3u8Process] doSyncRequestByM3U8Url:m3u8Url simulate:simulate start:YES];
    NSLog(@"%@",localM3u8String);
    if(localM3u8String!=nil){
        NSData *data = [localM3u8String dataUsingEncoding:NSUTF8StringEncoding];
        HTTPDataResponse* resp=[[HTTPDataResponse alloc] initWithData:data];
        return resp;
    }else{
        return nil;
    }
}
- (NSObject<HTTPResponse> *) handleProxyTempM3u8:(NSString*) path{
    NSString* m3u8Url = [[self parseGetParams] objectForKey:@"m3u8Url"];
    NSLog(@"m3u8Url=%@",m3u8Url);
    NSString* simulate = @"atv";
    if([request.url.host rangeOfString:@"localhost"].location!=NSNotFound){
        simulate = @"native";
    }
    
    NSRange range = [path rangeOfString:@"?"];
    path = [path substringWithRange:NSMakeRange(0, range.location)];
    NSString* localPath = [path stringByReplacingOccurrencesOfString:@"/appletv/noctl/proxy/temp/m3u8/" withString:[AppDele localM3u8PathPrefix]];
    
    if([[AppDele m3u8Process] m3u8Url]==nil||![[[AppDele m3u8Process] m3u8Url] isEqualToString:m3u8Url]){
        NSLog(@"m3u8Url is changed, we must reprocess it");
        [[AppDele m3u8Process] doSyncRequestByM3U8Url:m3u8Url simulate:simulate start:NO];
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
    return nil;
}
- (NSObject<HTTPResponse> *) handleMp4Play:(NSString*) path{
    NSString* mp4Url = [[self parseGetParams] objectForKey:@"url"];
    NSLog(@"mp4 url:%@",mp4Url);
    
    NSString* range = [request headerField:@"Range"];
    if(range==nil||[range length]==0){
        range = @"bytes=0-";
    }
    
    NSLog(@"Range:%@",range);
    NSArray* crs = [[range stringByReplacingOccurrencesOfString:@"bytes=" withString:@""] componentsSeparatedByString:@"-"];
    long startPosition = [(NSString*)[crs objectAtIndex:0] longLongValue];
    long endPosition = startPosition+MP4_PARTIAL_LENGTH-1;
    if([crs objectAtIndex:1]!=nil&&[(NSString*)[crs objectAtIndex:1] length]>0){
        endPosition = [(NSString*)[crs objectAtIndex:1] longLongValue];
    }
    if(endPosition-startPosition+1>MP4_PARTIAL_LENGTH){
        endPosition = startPosition+MP4_PARTIAL_LENGTH-1;
    }
    NSLog(@"Rage:%ld-%ld",startPosition,endPosition);
    
    if([mp4Url rangeOfString:@"smb://"].location!=NSNotFound){
        NSLog(@"smb url:%@",mp4Url);
        if(AppDele.smbProcess.smbPath==nil||![AppDele.smbProcess.smbPath isEqualToString:mp4Url]){
            AppDele.smbProcess.smbPath = mp4Url;
            if(AppDele.smbProcess.smbFile!=nil){
                [AppDele.smbProcess.smbFile close];
            }
            AppDele.smbProcess.smbFile = [KxSMBProvider fetchAtPath:mp4Url];
        }
        HTTPSMBResponse* resp=[[HTTPSMBResponse alloc] initWithSmbFile:AppDele.smbProcess.smbFile];
        return resp;
    }else if([mp4Url rangeOfString:@"file://"].location!=NSNotFound){
        NSString* filePath = [mp4Url stringByReplacingOccurrencesOfString:@"file://" withString:@""];
        NSLog(@"filePath=%@",filePath);
        HTTPMediaFileResponse* resp = [[HTTPMediaFileResponse alloc] initWithFilePath:filePath forConnection:self];
        [[resp httpHeaders] setValue:@"video/mp4" forKey:@"Content-Type"];
        [[resp httpHeaders] setValue:@"bytes" forKey:@"Accept-Ranges"];
        return resp;
    }else{
       [ProcessManager changeRunningProcess:@"mp4"];
        Mp4Download* mp4Download = [AppDele mp4Process].mp4Download;
        
        if(mp4Download!=nil&&[mp4Download.mp4Url isEqualToString:mp4Url]){
            
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
            [[resp httpHeaders] setValue:@"bytes" forKey:@"Accept-Ranges"];
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
    }
}
- (NSObject<HTTPResponse> *) handleXunleiGetSession:(NSString*) path{
    NSArray* cookies =[[NSHTTPCookieStorage sharedHTTPCookieStorage] cookies];
    NSString* sessionid = @"";
    NSString* lxsessionid = @"";
    NSString* vip = @"";
    NSString* userid = @"";
    NSString* gdriveid = @"";
    for(int i=0;i<[cookies count];i++){
        NSHTTPCookie* cookie = [cookies objectAtIndex:i];
        NSLog(@"%@ %@ %@",cookie.name,cookie.value,cookie.domain);
        if([cookie.domain rangeOfString:@".xunlei.com"].location!=NSNotFound){
            if([cookie.name isEqualToString:@"userid"]){
                userid = cookie.value;
            }else if([cookie.name isEqualToString:@"lsessionid"]){
                sessionid = cookie.value;
            }else if([cookie.name isEqualToString:@"isvip"]){
                vip = cookie.value;
            }else if([cookie.name isEqualToString:@"lx_sessionid"]){
                lxsessionid = cookie.value;
            }else if([cookie.name isEqualToString:@"gdriveid"]){
                gdriveid = cookie.value;
            }
        }
    }
    NSString* content = [NSString stringWithFormat:@"{\"sessionid\":\"%@\",\"userid\":\"%@\",\"vip\":\"%@\",\"lxsessionid\":\"%@\",\"gdriveid\":\"%@\"}",sessionid,userid,vip,lxsessionid,gdriveid];
    NSLog(@"%@",content);
    NSData *data = [content dataUsingEncoding:NSUTF8StringEncoding];
    HTTPDataResponse* resp=[[HTTPDataResponse alloc] initWithData:data];
    return resp;
}
- (NSObject<HTTPResponse> *) handlePhotoList:(NSString*) path{
    NSString* photos = [AppDele.photoProcess loadPhotos];
    NSData *data = [photos dataUsingEncoding:NSUTF8StringEncoding];
    HTTPDataResponse* resp=[[HTTPDataResponse alloc] initWithData:data];
    return resp;
}
- (NSObject<HTTPResponse> *) handlePhotoDetail:(NSString*) path{
    NSString* idStr = [[self parseGetParams] objectForKey:@"id"];
    NSData* data = [AppDele.photoProcess readPhoto:idStr];
    HTTPDataResponse* resp=[[HTTPDataResponse alloc] initWithData:data];
    return resp;
}
- (NSObject<HTTPResponse> *) handleMkvPlay:(NSString*) path{
    NSString* mkvUrl = [[self parseGetParams] objectForKey:@"url"];
    [ProcessManager changeRunningProcess:@"mkv"];
    NSString* filePath=[AppDele.mkvProcess convertToM3u8MkvUrl:mkvUrl];
    NSLog(@"m3u8Path:%@",filePath);
    NSString* content=nil;
    while(TRUE){
        if(![[NSFileManager defaultManager] fileExistsAtPath:filePath]){
            NSLog(@"The m3u8 file has not been created, wait for 3 second");
            [NSThread sleepForTimeInterval:3.0f];
        }else{
            content =[NSString stringWithContentsOfURL:[NSURL fileURLWithPath:filePath] encoding:NSUTF8StringEncoding error:nil];
            if(content!=nil&&[content rangeOfString:@"#EXTINF"].location!=NSNotFound){
                break;
            }else{
                NSLog(@"The m3u8 file is being created, wait for 3 second");
                [NSThread sleepForTimeInterval:3.0f];
            }
        }
        
    }
    
    
    if(content!=NULL){
        content = [content stringByReplacingOccurrencesOfString:AppDele.localMkvM3u8PathPrefix withString:AppDele.localMkvM3u8UrlPrefix];
    }
    NSLog(@"%@",content);
    NSData *data = [content dataUsingEncoding:NSUTF8StringEncoding];
    HTTPDataResponse* resp=[[HTTPDataResponse alloc] initWithData:data];
    return resp;
}
- (NSObject<HTTPResponse> *) handleProxyTempMkvM3u8:(NSString*) path{
    NSString* fileName = [path stringByReplacingOccurrencesOfString:@"/appletv/noctl/proxy/temp/mkvM3u8/" withString:@""];
    NSString* filePath = [AppDele.localMkvM3u8PathPrefix stringByAppendingString:fileName];
    if(![[NSFileManager defaultManager] fileExistsAtPath:filePath]){
        NSLog(@"ts for mkv is not found %@",fileName);
    }else{
        NSLog(@"ts for mkv is found %@",fileName);
    }
    HTTPFileResponse* resp = [[HTTPFileResponse alloc] initWithFilePath:filePath forConnection:self];
    return resp;
}
- (NSObject<HTTPResponse> *) handleProxySmb:(NSString*) path{
    NSString* smbUrl = [[self parseGetParams] objectForKey:@"url"];
    NSLog(@"smb url:%@",smbUrl);
    if(AppDele.smbProcess.smbPath==nil||![AppDele.smbProcess.smbPath isEqualToString:smbUrl]){
        AppDele.smbProcess.smbPath = smbUrl;
        if(AppDele.smbProcess.smbFile!=nil){
            [AppDele.smbProcess.smbFile close];
        }
        AppDele.smbProcess.smbFile = [KxSMBProvider fetchAtPath:smbUrl];
    }
    HTTPSMBResponse* resp=[[HTTPSMBResponse alloc] initWithSmbFile:AppDele.smbProcess.smbFile];
    return resp;
}
- (NSObject<HTTPResponse> *) handleSmbResource:(NSString*) path{
    BOOL sleep = NO;
    if(transfer_code_interrupt==0){
        sleep = YES;
    }
    transfer_code_interrupt = 1;
    
    if(sleep){
        [NSThread sleepForTimeInterval:3.0f];
    }
    AppDele.mkvProcess.mkvUrl=nil;
    @synchronized(AppDele.mkvProcess) {
        NSString* smbUrl = [[self parseGetParams] objectForKey:@"url"];
        NSString* content;
        if(smbUrl==nil||smbUrl.length==0){
            if(AppDele.auth!=nil&&AppDele.auth.serverIP!=nil){
                smbUrl = [NSString stringWithFormat:@"smb://%@",AppDele.auth.serverIP];
            }else{
                
            }
        }
        NSLog(@"smb url:%@",smbUrl);
        if(smbUrl!=NULL){
            content = [AppDele.smbProcess getResourcesForParent:smbUrl];
        }
        
        if(content==nil){
            content = @"{\"title\":\"无法访问smb共享\",\"description\":\"请先在设备上登录smb共享\"}";
        }
        NSData *data = [content dataUsingEncoding:NSUTF8StringEncoding];
        HTTPDataResponse* resp=[[HTTPDataResponse alloc] initWithData:data];
        return resp;
    }
}

- (NSObject<HTTPResponse> *) handleLocalResource{
        NSString* content = [AppDele.offlineRecordProcess getAllOfflineRecordByJsonFormat];
        NSData *data = [content dataUsingEncoding:NSUTF8StringEncoding];
        HTTPDataResponse* resp=[[HTTPDataResponse alloc] initWithData:data];
        return resp;
}

- (NSObject<HTTPResponse> *) handleSubTitle:(NSString*) path{
    NSString* fileUrl = [[self parseGetParams] objectForKey:@"url"];
    NSString* content;
    
    NSLog(@"file url:%@",fileUrl);
    if(fileUrl!=NULL){
        content = [AppDele.subTitleProcess downloadSubTitleByFile:fileUrl];
    }
    
    if(content!=nil){
        NSData *data = [content dataUsingEncoding:NSUTF8StringEncoding];
        HTTPDataResponse* resp=[[HTTPDataResponse alloc] initWithData:data];
        return resp;
    }else{
        HTTPDataResponse* resp=[[HTTPDataResponse alloc] initWithData:nil];
        return resp;
    }
}

@end
