//
//  DownloadProcess.m
//  appletvserver
//
//  Created by zhang wei on 13-5-29.
//
//

#import "DownloadProcess.h"
#import "Constants.h"
#import "AppDelegate.h"
#import "OfflineRecord.h"
#import "AtvUtil.h"
@implementation DownloadProcess

-(void) downloadOfflineRecord:(OfflineRecord*) offlineRecord{
    if([offlineRecord.fileType isEqualToString:FILE_TYPE_MP4]){
        ASIHTTPRequest *req = [ASIHTTPRequest requestWithURL:[NSURL URLWithString:offlineRecord.url]];
        offlineRecord.request = req;
        [req setShouldContinueWhenAppEntersBackground:YES];
        [req setDownloadProgressDelegate:offlineRecord];
        [req setShowAccurateProgress:YES];
        [req setDownloadDestinationPath:offlineRecord.filePath];
        [req setTemporaryFileDownloadPath:[offlineRecord.filePath stringByAppendingString:@".tmp"]];
        [req setAllowResumeForFileDownloads:YES];
        [req setTimeOutSeconds:180];
        [req setDidReceiveResponseHeadersSelector:@selector(request:didReceiveResponseHeaders:)];
        [req setDelegate:offlineRecord];
        [req startAsynchronous];
    }else{
        [NSThread detachNewThreadSelector:@selector(dowloadOfflineRecordInThread:) toTarget:self withObject:offlineRecord];
    }
    
}
-(void) dowloadOfflineRecordInThread:(OfflineRecord*) offlineRecord{
    NSRange lastSlahRange = [offlineRecord.url rangeOfString:@"/" options:NSBackwardsSearch];
    NSString* m3u8RelativeUrl;
    if(lastSlahRange.location!=NSNotFound){
        NSRange relativeRange = NSMakeRange(0,lastSlahRange.location);
        m3u8RelativeUrl = [offlineRecord.url substringWithRange:relativeRange];
    }else{
        m3u8RelativeUrl = offlineRecord.url;
    }
    NSString* m3u8String = [NSString stringWithContentsOfURL:[NSURL fileURLWithPath:offlineRecord.filePath] encoding:NSUTF8StringEncoding error:nil];
    
    NSArray* lines = [m3u8String componentsSeparatedByCharactersInSet: [NSCharacterSet newlineCharacterSet]];
    for(int i=0;i<[lines count]&&offlineRecord.downloading;i++){
        NSString* line = [lines objectAtIndex:i];
        if(line!=nil&&[line length]!=0&&[line rangeOfString:@"#"].location!=0&&![AtvUtil content:line startWith:[@"http://" stringByAppendingString:AppDele.ipAddress]]){
            if(![AtvUtil isWifi]&&!AppDele.ttgNetwork){
                NSLog(@"2G/3G下无法继续download");
                offlineRecord.downloading=FALSE;
                break;
            }
            NSLog(@"download %@",line);
            ASIHTTPRequest *req = [ASIHTTPRequest requestWithURL:[NSURL URLWithString:line]];
            offlineRecord.request = req;
            [req setTimeOutSeconds:180];
            [req setShouldContinueWhenAppEntersBackground:YES];
            [req setDownloadProgressDelegate:offlineRecord];
            [req startSynchronous];
            
            NSError* error = req.error;
            if(error){
                offlineRecord.downloading = FALSE;
                break;
            }
            NSData* data = [req responseData];
            if(data.length==0){
                offlineRecord.downloading = FALSE;
                break;
            }
            NSString* dataFilePath = [offlineRecord.filePath stringByAppendingString:@".data"];
            NSLog(@"dataFilePath:%@",dataFilePath);
            long s = 0;
            if(![[NSFileManager defaultManager] fileExistsAtPath:dataFilePath]){
                [data writeToFile:dataFilePath atomically:YES];
            }else{
                NSFileHandle* fileHandler= [NSFileHandle fileHandleForWritingAtPath:[offlineRecord.filePath stringByAppendingString:@".data"]];
                s = [fileHandler seekToEndOfFile];
                [fileHandler writeData:data];
                [fileHandler closeFile];
            }
           
            long e = [data length];
            NSLog(@"write m3u8 ts file %ld-%ld",s,s+e);
            NSString* localUrl = [NSString stringWithFormat:@"http://%@:8080/appletv/noctl/local/m3u8?m3u8DataPath=%@&s=%ld&l=%ld",AppDele.ipAddress,[AtvUtil encodeURL:[@"file://" stringByAppendingString:[offlineRecord.filePath stringByAppendingString:@".data"]]],s,e];
            m3u8String = [m3u8String stringByReplacingOccurrencesOfString:line withString:localUrl];
            [m3u8String writeToFile:offlineRecord.filePath atomically:YES encoding:NSUTF8StringEncoding error:nil];
            offlineRecord.downloadFileSize++;
        }
        
    }
}
-(void) downloadMp4:(NSString*) mp4Url{
    NSLog(@"download mp4:%@",mp4Url);
    OfflineRecord* record = [[[OfflineRecord alloc] init] autorelease];
    record.url = mp4Url;
    record.fileType = FILE_TYPE_MP4;
    record.fileName = [[AtvUtil getUUID] stringByAppendingString:@".mp4"];
    record.filePath = [AppDele.localDownloadPathPrefix stringByAppendingPathComponent:record.fileName];
    record.displayName = record.url;
    if(![AtvUtil isWifi]&&!AppDele.ttgNetwork){
        record.downloading = NO;
    }else{
        record.downloading = YES;
    }
    
    [AppDele.offlineRecordProcess insertOrUpdateOffileRecord:record];
    [self downloadOfflineRecord:record];
}
-(void) downloadM3u8:(NSString*) m3u8Url{
    NSLog(@"download m3u8:%@",m3u8Url);
    OfflineRecord* record = [[[OfflineRecord alloc] init] autorelease];
    record.url = m3u8Url;
    record.fileType = FILE_TYPE_M3U8;
    record.fileName = [[AtvUtil getUUID] stringByAppendingString:@".m3u8"];
    record.filePath = [AppDele.localDownloadPathPrefix stringByAppendingPathComponent:record.fileName];
    record.displayName = record.url;
    if(![AtvUtil isWifi]&&!AppDele.ttgNetwork){
        record.downloading = NO;
    }else{
        record.downloading = YES;
    }
    [AppDele.offlineRecordProcess insertOrUpdateOffileRecord:record];
    ASIHTTPRequest *req = [ASIHTTPRequest requestWithURL:[NSURL URLWithString:record.url]];
    [req setDownloadDestinationPath:record.filePath];
    [req setShouldContinueWhenAppEntersBackground:YES];
    [req setTimeOutSeconds:180];
    [req startSynchronous];
    
    NSString* m3u8String = [NSString stringWithContentsOfURL:[NSURL fileURLWithPath:record.filePath] encoding:NSUTF8StringEncoding error:nil];
    
    NSArray* lines = [m3u8String componentsSeparatedByCharactersInSet: [NSCharacterSet newlineCharacterSet]];
    for(int i=0;i<[lines count];i++){
        NSString* line = [lines objectAtIndex:i];
        if(line!=nil&&[line length]!=0&&[line rangeOfString:@"#"].location!=0){
            record.fileSize++;
        }
    }
    [self downloadOfflineRecord:record];
}
@end
