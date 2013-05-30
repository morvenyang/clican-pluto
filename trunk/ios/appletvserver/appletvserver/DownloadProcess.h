//
//  DownloadProcess.h
//  appletvserver
//
//  Created by zhang wei on 13-5-29.
//
//

#import <Foundation/Foundation.h>

@interface DownloadProcess : NSObject
-(void) downloadMp4:(NSString*) mp4Url;
-(void) downloadM3u8:(NSString*) m3u8Url;
@end
