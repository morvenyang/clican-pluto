//
//  M3u8Download.m
//  appletvserver
//
//  Created by zhang wei on 13-2-27.
//
//

#import "M3u8Download.h"
#import "AtvUtil.h"
#import "AppDelegate.h"
@implementation M3u8Download

@synthesize m3u8DownloadLines = _m3u8DownloadLines;
@synthesize m3u8Url = _m3u8Url;
@synthesize tempSize = _tempSize;


-(long) getDurationTempSize{
    long temp = [AtvUtil getFolderSize:AppDele.localM3u8PathPrefix];
    long result = temp-_tempSize;
    _tempSize = temp;
    return result;
}
-(void) seekDownloadLine:(NSString*) localPath{
    @synchronized(self) {
        for(int i=0;i<[_m3u8DownloadLines count];i++){
            M3u8DownloadLine* downloadLine=[_m3u8DownloadLines objectAtIndex:i];
            if([downloadLine.localPath isEqualToString:localPath]){
                _downloadIndex = i;
                break;
            }
        }
    }
}
- (M3u8DownloadLine*) getNextDownloadLine{
     @synchronized(self) {
         if(_downloadIndex<[_m3u8DownloadLines count]){
             M3u8DownloadLine* next=[_m3u8DownloadLines objectAtIndex:_downloadIndex];
             _downloadIndex++;
             return next;
         }else{
             return nil;
         }
     }
}


- (void) dealloc {
    TT_RELEASE_SAFELY(_m3u8DownloadLines);
    TT_RELEASE_SAFELY(_m3u8Url);
    [super dealloc];
}

@end
