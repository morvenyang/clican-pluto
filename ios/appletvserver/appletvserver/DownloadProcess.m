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
}

-(void) downloadMp4:(NSString*) mp4Url{
    NSLog(@"download mp4:%@",mp4Url);
    OfflineRecord* record = [[[OfflineRecord alloc] init] autorelease];
    record.url = mp4Url;
    record.fileType = FILE_TYPE_MP4;
    record.fileName = [[AtvUtil getUUID] stringByAppendingString:@".mp4"];
    record.filePath = [AppDele.localDownloadPathPrefix stringByAppendingPathComponent:record.fileName];
    record.displayName = record.url;
    record.downloading = YES;
    [AppDele.offlineRecordProcess insertOrUpdateOffileRecord:record];
    [self downloadOfflineRecord:record];
}
-(void) downloadM3u8:(NSString*) m3u8Url{
    NSLog(@"download m3u8:%@",m3u8Url);
}

@end
