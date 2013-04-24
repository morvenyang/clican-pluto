//
//  DownloadStatusDataSource.h
//  appletvserver
//
//  Created by zhang wei on 13-3-20.
//
//

#import <Three20UI/Three20UI.h>
#import "DownloadStatusModel.h"

@interface DownloadStatusDataSource : TTListDataSource{
    DownloadStatusModel* _downloadStatusModel;
}
@property (nonatomic,retain) DownloadStatusModel* downloadStatusModel;
@end
