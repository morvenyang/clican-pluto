//
//  PhotoProcess.h
//  appletvserver
//
//  Created by zhang wei on 13-3-29.
//
//

#import <Foundation/Foundation.h>
#import <AssetsLibrary/AssetsLibrary.h>
@interface PhotoProcess : NSObject{
    NSMutableArray* _assets;
    ALAssetsLibrary* _library;
}

@property (nonatomic, retain) NSMutableArray    *assets;
@property (nonatomic, retain) ALAssetsLibrary    *library;
-(NSString*) loadPhotos;
-(NSData*) readPhoto:(NSString*) idStr;
@end
