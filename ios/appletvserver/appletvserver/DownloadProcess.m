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



-(void) downloadMp4:(NSString*) mp4Url{
    NSLog(@"download mp4:%@",mp4Url);
    OfflineRecord* record = [[[OfflineRecord alloc] init] autorelease];
    record.url = mp4Url;
    record.fileType = FILE_TYPE_MP4;
    record.fileName = [[AtvUtil getUUID] stringByAppendingString:@".mp4"];
    record.filePath = [AppDele.localDownloadPathPrefix stringByAppendingPathComponent:record.fileName];
    record.displayName = record.url;
    [AppDele.dbProcess insertOrUpdateOffileRecord:record];
    ASIHTTPRequest *req = [ASIHTTPRequest requestWithURL:[NSURL URLWithString:mp4Url]];
    [req setShouldContinueWhenAppEntersBackground:YES];
    [req setDownloadProgressDelegate:self];
    [req setShowAccurateProgress:YES];
    [req setDownloadDestinationPath:record.filePath];
    [req setAllowResumeForFileDownloads:YES];
    [req setTimeOutSeconds:180];
    [req setDelegate:self];
    [req setDidFinishSelector:@selector(mp4RequestDone:)];
    [req setDidFailSelector:@selector(mp4RequestWentWrong:)];
    [[AppDele downloadQueue] addOperation:req];
}
-(void) downloadM3u8:(NSString*) m3u8Url{
    NSLog(@"download m3u8:%@",m3u8Url);
}

@end
