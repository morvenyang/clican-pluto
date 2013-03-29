//
//  M3u8Process.h
//  appletvserver
//
//  Created by zhang wei on 13-2-27.
//
//

#import <Foundation/Foundation.h>
#import "M3u8Download.h"

@interface M3u8Process : NSObject{
    NSString* _m3u8Url;
    NSString* _m3u8RelativeUrl;
    M3u8Download* _m3u8Download;
    NSString* _m3u8String;
    BOOL _running;
}

@property (nonatomic, copy) NSString* m3u8Url;
@property (nonatomic, copy) NSString* m3u8RelativeUrl;
@property (nonatomic, retain) M3u8Download* m3u8Download;
@property (nonatomic, copy) NSString* m3u8String;
@property (nonatomic, assign) BOOL running;

-(NSString*) doSyncRequestByM3U8Url:(NSString*) url simulate:(NSString*)simulate start:(BOOL) start;
-(void) seekDownloadLine:(NSString*) localUrl;
-(void) start;
-(void) addAsyncM3u8TSRequest;

@end
