//
//  Mp4DownloadPartial.h
//  appletvserver
//
//  Created by zhang wei on 13-2-28.
//
//

#import <Foundation/Foundation.h>

@interface Mp4DownloadPartial : NSObject{
    long _startPosition;
    long _endPosition;
    BOOL _finished;
    NSString* _localPath;
}

@property (nonatomic, assign) long startPosition;
@property (nonatomic, assign) long endPosition;
@property (nonatomic, assign) BOOL finished;
@property (nonatomic, copy) NSString* localPath;
@end
