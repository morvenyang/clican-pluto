//
//  Mp4Process.h
//  appletvserver
//
//  Created by zhang wei on 13-2-28.
//
//

#import "Mp4Download.h"

@interface Mp4Process : NSObject{
    NSString* _mp4Url;
    Mp4Download* _mp4Download;
}

@property (nonatomic, copy) NSString* mp4Url;
@property (nonatomic, retain) Mp4Download* mp4Download;

-(Mp4Download*) doSyncRequestByMP4Url:(NSString*) url;
-(void) addAsyncMp4Request;

@end
